from typing import List

from common import generate_id
from common.models import Chat
from common.models import ChatRoles
from common.models import ChatTypes
from common.models import Member
from common.models import Message
from common.models import MessageTypes
from config import myconnect
from config import mydb


class ChatRepository:
    def __init__(self, id=None, owner_id=None, chat_type=ChatTypes.PERSON):
        if id is None:
            if owner_id is None:
                raise ValueError

            self.chat = Chat(id=generate_id(), chat_type=chat_type)
            mydb.execute('''create table messages_{} 
            (number int auto_increment primary key, id varchar(10), from_user varchar(10), message_type varchar(20), metadata text) 
            '''.format(self.chat.id))
            mydb.execute('''create table members_{} 
            (id varchar(10), role varchar(20)) 
            '''.format(self.chat.id))

            mydb.execute("insert into members_{} (id, role) values (%s, %s)".format(self.chat.id), Member(id=owner_id,
                            role=ChatRoles.OWNER if chat_type == ChatTypes.GROUP else ChatRoles.DEFAULT).to_dataraw())
            mydb.execute("insert into chats (id, chat_type, metadata) values (%s, %s, %s)",
                         self.chat.to_dataraw())
            myconnect.commit()
        else:
            mydb.execute("select * from chats where id=%s", (id, ))
            self.chat = Chat(*mydb.fetchall()[0])

    @property
    def messages(self):
        mydb.execute('''select * from messages_{}'''.format(self.chat.id))
        messages = mydb.fetchall()
        return [Message(*template) for template in messages]

    @property
    def members(self):
        mydb.execute('''select * from members_{}'''.format(self.chat.id))
        members = mydb.fetchall()
        return [Member(*template) for template in members]

    def person_role(self, person_id):
        mydb.execute('''select (role) from members_{} where id = %s'''.format(self.chat.id), (person_id,))
        return mydb.fetchall()[0][0]

    def member_ids(self):
        return [member.id for member in self.members]

    def get_messages(self, person_id) -> List[Message]:
        person_role = self.person_role(person_id)
        messages = []
        for message in self.messages:
            if person_role == ChatRoles.OWNER or self.chat.chat_type == ChatTypes.PERSON or \
                    person_role == ChatRoles.DEFAULT and message.message_type == MessageTypes.DEFAULT or \
                    person_role == ChatRoles.UNAPPROVED and message.from_user == person_id:
                messages.append(message)

        return messages

    def get_members(self, person_id) -> List[int]:
        person_role = self.person_role(person_id)
        members = []
        for member in self.members:
            if person_role == ChatRoles.OWNER or self.chat.chat_type == ChatTypes.PERSON or \
                    person_role == ChatRoles.DEFAULT and member.role != ChatRoles.UNAPPROVED or \
                    person_role == ChatRoles.UNAPPROVED and member.role == ChatRoles.OWNER:
                members.append(member.id)

        return members

    def send_message(self, person_id, message_type, metadata=None) -> Message:
        if metadata is None:
            metadata = dict()
        person_role = self.person_role(person_id)
        print(person_role)
        if person_role == ChatRoles.UNAPPROVED and message_type != MessageTypes.INVITE:
            return None
        if message_type == MessageTypes.INVITE:
            metadata["accepted"] = False

        message = Message(from_user=person_id,
                          message_type=message_type, metadata=metadata)
        mydb.execute('''insert into messages_{} (id, from_user, message_type, metadata)
                        values (%s, %s, %s, %s)
                    '''.format(self.chat.id), message.to_dataraw(skip_fields=["number"]))
        myconnect.commit()
        return self.messages[-1]

    def add_member(self, person_id, member_id, member_role) -> Member:
        person_role = self.person_role(person_id)

        if not (self.chat.chat_type == ChatTypes.PERSON and len(self.members) == 1 and member_role == ChatRoles.DEFAULT or
                self.chat.chat_type == ChatTypes.GROUP and person_role == ChatRoles.OWNER and member_role == ChatRoles.UNAPPROVED):
            return None
        if member_id in self.member_ids():
            return None

        member = Member(id=member_id, role=member_role)
        mydb.execute("insert into members_{} (id, role) values (%s, %s)".format(self.chat.id), member.to_dataraw())
        myconnect.commit()
        return member

    def join_chat(self, member_id, member_role) -> Member:
        if self.chat.chat_type != ChatTypes.GROUP or member_role == ChatRoles.OWNER:
            return None

        member = Member(id=member_id, role=member_role)
        mydb.execute("insert into members_{} (id, role) values (%s, %s)".format(self.chat.id), member.to_dataraw())
        myconnect.commit()
        return member

create database springcoders;
use springcoders;
drop database springcoders;

create table user(
                     userid varchar(300) primary key,
                     userpw varchar(300) not null,
                     username varchar(300) not null,
                     userphone varchar(11) unique,
                     usergender char(3) not null,
                     useremail varchar(300) unique,
                     zipcode varchar(300) not null,
                     addr varchar(1000),
                     addrdetail varchar(1000) not null,
                     addretc varchar(300),
                     userprofile varchar(300) default 'planz',
                     usercoin int default 0,
                     category varchar(1000) not null #선호하는 여행 카테고리
);

insert into user(userid, userpw, username, userphone, useremail, usergender, zipcode, addrdetail, category) values('NAVERGrUZOA02kV_JlVDA8iXDkMASW1OwBOB48s8GRnS7dfk', '8ZbR3B4sbo', '윤종수', '01026897358', 'bluesea15333@gmail.com', '남자', '용인', '처인구', '여행');
insert into user(userid, userpw, username, userphone, useremail, usergender, zipcode, addrdetail, category) values('GOOGLEanjtlrlanjtlrl', '8ZbR3B4sbo', '김일환', '01026897359', 'bluesea15332@gmail.com', '남자', '용인', '처인구', '여행');
insert into user(userid, userpw, username, userphone, useremail, usergender, zipcode, addrdetail, category) values('KAKAOanjtlrlanjtlrl', '8ZbR3B4sbo', '김일환2', '01026897350', 'bluesea15334@gmail.com', '남자', '용인', '처인구', '여행');
insert into user(userid, userpw, username, userphone, useremail, usergender, zipcode, addrdetail, category) values('rladlfghks', '8ZbR3B4sbo', '김일환3', '01026897351', 'bluesea15335@gmail.com', '남자', '용인', '처인구', '여행');
insert into user(userid, userpw, username, userphone, useremail, usergender, zipcode, addrdetail, category) values('rladlfghks1', '8ZbR3B4sbo', '김일환4', '01026897352', 'bluesea15336@gmail.com', '남자', '용인', '처인구', '여행');

select * from user;

create table follow(
                       follower varchar(300) references user(userid),
                       following varchar(300) references user(userid)
);

create table plan(
                     plannum bigint primary key auto_increment,
                     plantitle varchar(1000) not null,
                     planmemo text,
                     startdate varchar(10) not null, #ex) 2024_05_09
    enddate varchar(10) not null,
    category varchar(1000) not null  #여행의 주 목적
);

create table user_plan(
                          userid varchar(300),
                          plannum bigint,
                          primary key (userid,plannum)
);

create table schedule(
                         schedulenum bigint primary key auto_increment,
                         scheduletitle varchar(100) not null,
                         schedulememo text,
                         starttime varchar(16) not null, #ex) 2024_05_09_hh:mm
    endtime varchar(16) not null,
    plannum bigint references plan(plannum)
);

select t.* from schedule s join time t on s.schedulenum = t.schedulenum;

create table place(
                      placeid varchar(300) primary key,
                      placename varchar(300) not null,
                      addr varchar(1000) not null,
                      roadaddr varchar(1000),
                      phone varchar(13),
                      category varchar(500) not null
);


create table reply(
                      replynum bigint primary key auto_increment,
                      replycontents varchar(2000),
                      regdate datetime default now(),
                      updatedate datetime default now(),
                      plannum bigint references plan(plannum),
                      userid varchar(300) references user(userid)
);

create table file(
                     systemname varchar(2000),
                     orgname varchar(2000),
                     fileid varchar(300) #ex) userid_'apple', plan_10
);

create table payment(
                        paymentnum int auto_increment primary key,
                        userid varchar(300) references user(userid),
                        payment_coin int,
                        payment_date datetime default now()
);

create table chatroom(
	roomno bigint primary key auto_increment,
    roomname varchar(300) not null,
    bannuser varchar(300) references user(userid),
    adminid varchar(300) references user(userid),
    roomtype varchar(300) default 'chat'
);

create table chatuser(
	roomno bigint references chatroom(roomno),
    roomname varchar(300) not null,
    userid varchar(300) references user(userid),
    last_read_chatno int default 0 references chatmessage(chatno),
    CONSTRAINT unique_roomname_userid UNIQUE (roomname, userid)
);


create table chatmessage(
	chatno bigint primary key auto_increment,
    type varchar(20) not null,
    roomno int not null references chatroom(roomno),
    userid varchar(300) not null references user(userid),
    message varchar(2000) not null,
    time timestamp default current_timestamp
);


create database springcoders;
use springcoders;

create table user(
	userid varchar(300) primary key,
    userpw varchar(300) not null,
    username varchar(300) not null,
    userphone varchar(13) unique,
    useremail varchar(300) unique,
    usergender char(3) not null,
    zipcode varchar(300) not null,
    addr varchar(1000),
    addrdetail varchar(1000) not null,
    addretc varchar(300),
    userprofile varchar(300) default 'planz', #유저사진
    usercoin int default 0,
    category varchar(1000) not null #선호하는 여행 카테고리
);
drop database springcoders;
select * from user;

create table plan(
	plannum bigint primary key auto_increment,
    plantitle varchar(1000) not null,
    planmemo text,
    startdate varchar(10) not null, #ex) 2024_05_09
    enddate varchar(10) not null,
    category varchar(1000) not null  #여행의 주 목적
);
select * from plan;
drop table plan;

create table user_plan(
	userid varchar(300) references user(userid),
	plannum bigint references plan(plannum),
    part varchar(300),
    primary key (userid,plannum) 
);
select * from user_plan;
drop table user_plan;

create table plan_place(
	plannum bigint references plan(plannum),
	placeid bigint references place(placeid),
    primary key (plannum,placeid) 
);
drop table plan_place;

create table schedule(
	schedulenum bigint primary key auto_increment,
    scheduletitle varchar(100) not null,
    schedulememo text,
	starttime varchar(16) not null, #ex) 2024_05_09_hh:mm
    endtime varchar(16) not null,
    plannum bigint references plan(plannum)
);
select * from schedule;
drop table schedule;

create table place(
	placeid bigint primary key,
    placename varchar(300) not null,
    addr varchar(1000) not null,
    phone varchar(20),
    category varchar(500) not null
);
drop table place;
select * from place;
#place_url = http://place.map.kakao.com/ + placeid

create table user_place(
    userid varchar(300) references user(userid),
    placeid bigint references place(placeid),
    primary key (userid,placeid)
);
select * from user_place;


create table reply(
   replynum bigint primary key auto_increment,
    replycontents varchar(2000),
   regdate datetime default now(),
    updatedate datetime default now(),
    userid varchar(300) references user(userid)
);

create table place_reply(
    placeid bigint references place(placeid),
    replynum bigint references reply(replynum),
    primary key (placeid,replynum)
);

create table file(
	systemname varchar(2000),
    orgname varchar(2000),
    fileid varchar(300) #ex) userid_'apple', plan_10
);
select * from file;
delete from file where fileid = 'place_25039713';
SET SQL_SAFE_UPDATES = 1;
update file set systemname = 'no_image.jpg' where systemname = 'no_image.jph';
update file set systemname = 'no_image.jpg' where fileid = 'place_25039713';
update file set systemname = 'no_image.jpg' where fileid = 'place_25039713';
drop table file;

create table category(
	categoryname varchar(500) primary key
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
    userid varchar(300),
    last_read_chatno int default 0 references chatmessage(chatno),
    CONSTRAINT unique_roomno_userid UNIQUE (roomno, userid)
);

create table chatmessage(
   chatno bigint primary key auto_increment,
    type varchar(20) not null,
    roomno int not null,
    userid varchar(300) not null,
    message varchar(2000) not null,
    time timestamp default current_timestamp
);



drop table chatmessage;
select * from chatmessage;


create table board (
   boardnum bigint primary key auto_increment,
   boardtitle varchar(100),
    boardcontents varchar(1000),
    travelPlansStart datetime,
    travelPlansEnd datetime,
    updatedate datetime default now(),
    created datetime default now(),
    price bigint,
    userid varchar(300) references user(userid)
);

create table deal(
                     boardnum bigint references board(boardnum),
                     userid varchar(300) references user(userid), # 구매자
                         buystate varchar(300) default 'X', #구매자 사용여부 상태
                         dealdate datetime default now() # 여행일정 마지막 날로부터 10일 이후 상태 변경 없을 시, 자동으로 승인 ( X -> O)

);
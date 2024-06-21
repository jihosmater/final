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
                     userprofile varchar(300) default '', #유저사진
                         usercoin int default 0,
                     category varchar(1000) not null #선호하는 여행 카테고리
);
drop table user;
select * from user;
insert into user(userid, userpw, username, userphone, useremail,  usergender, zipcode, addr, addrdetail, addretc, category)
values('apple','1234','김사과','010-1234-5678','apple@naver.com','남','12345','서울 강남구 논현동','아파트 1004호','(논현동)','');
insert into user(userid, userpw, username, userphone, useremail,  usergender, zipcode, addr, addrdetail, addretc, category)
values('banana','1234','반하나','010-2345-6789','banana@naver.com','남','12345','서울 강남구 논현동','아파트 1004호','(논현동)','');
insert into user(userid, userpw, username, userphone, useremail,  usergender, zipcode, addr, addrdetail, addretc, category)
values('cherry','1234','박체리','010-3456-7890','cherry@naver.com','남','12345','서울 강남구 논현동','아파트 1004호','(논현동)','');
insert into user(userid, userpw, username, userphone, useremail,  usergender, zipcode, addr, addrdetail, addretc, category)
values('melon','1234','왕멜론','010-4567-8901','melon@naver.com','남','12345','서울 강남구 논현동','아파트 1004호','(논현동)','');
insert into user(userid, userpw, username, userphone, useremail,  usergender, zipcode, addr, addrdetail, addretc, category)
values('podo','1234','대포도','010-5431-8901','podo@naver.com','남','12345','서울 강남구 논현동','아파트 1004호','(논현동)','');
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
                      placeid bigint primary key,
                      placename varchar(300) not null,
                      addr varchar(1000) not null,
                      phone varchar(20),
                      category varchar(500) not null
);
drop table place;
alter table place modify placeid bigint;
#place_url = http://place.map.kakao.com/ + placeid
select * from place where addr != '주소';
insert into place (select * from place);
select * from place;
insert into place(placeid, placename,addr,category) values ('1','장소이름', '주소','식당');

create table sample(
                       placeid int primary key auto_increment,
                       placename varchar(300) not null,
                       addr varchar(1000) not null,
                       category varchar(500) not null
);
insert into sample( placename,addr,category) values ('장소이름', '주소','식당');

create table user_place(
                           userid varchar(300) references user(userid),
                           placeid int references place(placeid),
                           primary key (userid,placeid)
);
drop table user_place;
select * from user_place;
insert into user_place values('banana',13575898);
insert into user_place values('cherry',13575898);

create table plan_place(
                           plannum bigint references plan(plannum),
                           placeid bigint references place(placeid),
                           primary key (plannum,placeid)
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
select * from file;
drop table file;

select p.*,count(userid) from place p left join user_place up
                                                on p.placeid = up.placeid
group by p.placeid order by count(up.userid) desc;

select count(userid) from place p left join user_place up
                                            on p.placeid = up.placeid
group by p.placeid order by count(up.userid) desc;

select p.* from place p left join user_place up
                                  on p.placeid = up.placeid
group by placeid order by count(up.userid) desc;

create table category(
                         categoryname varchar(500) primary key
);
select * from category;


select p.* from place p left join user_place up
                                  on p.placeid = up.placeid
where p.category like ('%식당%')
  and (placename like('%%') or category like('%%'))
group by p.placeid;

select p.* from place p left join user_place up
                                  on p.placeid = up.placeid
where up.userid = 'apple'
  and (placename like('%%') or category like('%%'))
group by p.placeid;

select count(DISTINCT p.placeid) from place p left join user_place up
                                                        on p.placeid = up.placeid
where p.category like ('%식당%')
  and (placename like('%%') or category like('%%'));

select count(DISTINCT p.placeid) from place p left join user_place up
                                                        on p.placeid = up.placeid
where up.userid = 'apple'
  and (placename like('%%') or category like('%%'));

select count(up.userid) from place p left join user_place up
                                               on p.placeid = up.placeid
where p.category like ('%명소%')
  and (placename like('%%') or category like('%%'))
group by p.placeid order by count(up.userid) desc limit 0,10;

select * from place;
select * from file;
select * from user_place;
select count(*) from user_place where userid = 'apple' and placeid = 17733090;
create table plan_place(
                           plannum bigint references plan(plannum),
                           placeid bigint references place(placeid),
                           primary key (plannum,placeid)
);
INSERT INTO role (name, description) VALUES ('user', '用户');
INSERT INTO role (name, description) VALUES ('maintainer', '检修人员');
INSERT INTO role (name, description) VALUES ('admin', '管理员');


select
  user0_.id              as id1_8_,
  user0_.avatar          as avatar2_8_,
  user0_.description     as descript3_8_,
  user0_.email           as email4_8_,
  user0_.is_email_locked as is_email5_8_,
  user0_.is_locked       as is_locke6_8_,
  user0_.name            as name7_8_,
  user0_.password        as password8_8_,
  user0_.phone           as phone9_8_,
  user0_.username        as usernam10_8_
from user user0_ inner join user_role roles1_ on user0_.id = roles1_.uid
  inner join role role2_ on roles1_.role_id = role2_.id
where role2_.id = ? and (user0_.username like ?)
order by ?, user0_.id asc

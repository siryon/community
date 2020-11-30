CREATE  TABLE question(
    id INT AUTO_INCREMENT primary key  NOT NULL ,
    title VARCHAR(50),
    description text,
    gmt_create bigint,
    gmt_modified bigint,
    creator int,
    comment_count int default 0,
    view_count int default 0,
    like_count int default 0,
    tag varchar(256)

);
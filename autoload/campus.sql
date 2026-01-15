-- campus.sql  (UTF-8)

-- 关键：临时禁用 ONLY_FULL_GROUP_BY
SET SESSION sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

DROP DATABASE IF EXISTS campus;
CREATE DATABASE campus CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus;

CREATE TABLE user(
  id      INT PRIMARY KEY AUTO_INCREMENT,
  account VARCHAR(20) UNIQUE NOT NULL,
  pwd     VARCHAR(20) NOT NULL,
  name    VARCHAR(30) NOT NULL,
  role    ENUM('student','admin') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO user VALUES
(1,'admin','123','张主任','admin'),
(2,'stu1','123','李同学','student'),
(3,'stu2','123','王同学','student');

CREATE TABLE activity(
  id          INT PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(100) NOT NULL,
  info        TEXT,
  start_time  DATETIME NOT NULL,
  end_time    DATETIME NOT NULL,
  location    VARCHAR(100),
  max_num     INT NOT NULL CHECK (max_num>0),
  status      ENUM('未开始','报名中','已结束','已取消') DEFAULT '报名中',
  admin_id    INT NOT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_act_admin FOREIGN KEY (admin_id) REFERENCES user(id),
  CONSTRAINT chk_time CHECK (end_time>start_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE apply(
  id         INT PRIMARY KEY AUTO_INCREMENT,
  stu_id     INT NOT NULL,
  act_id     INT NOT NULL,
  apply_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  check_time DATETIME,
  score      TINYINT CHECK (score BETWEEN 1 AND 5),
  comment    VARCHAR(500),
  UNIQUE(stu_id,act_id),
  CONSTRAINT fk_apply_stu FOREIGN KEY (stu_id) REFERENCES user(id),
  CONSTRAINT fk_apply_act FOREIGN KEY (act_id) REFERENCES activity(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DELIMITER $$
CREATE TRIGGER trg_apply_insert
BEFORE INSERT ON apply
FOR EACH ROW
BEGIN
  DECLARE curr INT DEFAULT 0;
  DECLARE lim  INT DEFAULT 0;
  SELECT COUNT(*), a.max_num INTO curr, lim
  FROM apply ap
  JOIN activity a ON a.id = NEW.act_id
  WHERE ap.act_id = NEW.act_id;
  IF curr >= lim THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '报名人数已满';
  END IF;
END$$
DELIMITER ;

-- 核心：添加 location 列，并优化结构
DROP VIEW IF EXISTS v_act_stats;
CREATE VIEW v_act_stats AS
SELECT 
    a.id,
    MAX(a.name) AS name,
    MAX(a.info) AS info,
    MAX(a.start_time) AS start_time,
    MAX(a.end_time) AS end_time,
    MAX(a.location) AS location,  -- ← 关键：添加这一列
    MAX(a.max_num) AS max_num,
    MAX(CASE 
        WHEN a.status = '已取消' THEN '已取消'
        WHEN NOW() < a.start_time THEN '未开始'
        WHEN NOW() > a.end_time THEN '已结束'
        ELSE '报名中'
    END) AS status,
    COALESCE(ap.cnt, 0) AS apply_num,
    COALESCE(ch.cnt, 0) AS check_num,
    COALESCE(sc.avg_s, 0) AS avg_score
FROM activity a
LEFT JOIN (
    SELECT act_id, COUNT(*) AS cnt 
    FROM apply 
    GROUP BY act_id
) ap ON ap.act_id = a.id
LEFT JOIN (
    SELECT act_id, COUNT(*) AS cnt 
    FROM apply 
    WHERE check_time IS NOT NULL 
    GROUP BY act_id
) ch ON ch.act_id = a.id
LEFT JOIN (
    SELECT act_id, AVG(score) AS avg_s 
    FROM apply 
    WHERE score IS NOT NULL 
    GROUP BY act_id
) sc ON sc.act_id = a.id
GROUP BY a.id;

-- 插入测试数据（时间已过期）
INSERT INTO activity(name, info, start_time, end_time, location, max_num, status, admin_id) VALUES
('Campus Singer', 'Sing for youth', '2026-03-20 14:00:00', '2026-03-20 17:00:00', 'Hall', 2, '报名中', 1),
('Basketball Match', '3v3 game', '2026-03-20 15:00:00', '2026-03-20 18:00:00', 'Gym', 3, '报名中', 1);

INSERT INTO apply(stu_id, act_id) VALUES (2,1),(2,2);

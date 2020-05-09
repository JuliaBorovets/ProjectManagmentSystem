INSERT INTO `proj_man_sys`.`project` (`description`, `name`)
VALUES ('djdjjd', 'kdkdkd');
INSERT INTO `proj_man_sys`.`project` (`description`, `name`)
VALUES ('dkkdkd', 'kdkdkd');
INSERT INTO `proj_man_sys`.`project` (`description`, `name`)
VALUES ('kdkdkd', 'dkdkd');

INSERT INTO `proj_man_sys`.`task` (`deadline`, `description`, `name`, `project_id`)
VALUES ('20/12/2020', 'dkfk', 'kskdkd', '1');
INSERT INTO `proj_man_sys`.`task` (`deadline`, `description`, `name`, `project_id`)
VALUES ('14/03/2030', 'kdkfk', 'kskdkd', '1');

INSERT INTO `proj_man_sys`.`artifact` (`content`, `name`, `type`, `task_id`)
VALUES ('dkkd', 'dkkd', 'dkkd', '1');
INSERT INTO `proj_man_sys`.`artifact` (`content`, `name`, `type`, `task_id`)
VALUES ('dkkd', 'ksdk', 'kssdk', '1');

INSERT INTO proj_man_sys.project_worker(project_id, worker_id)
VALUES (1, 1);
INSERT INTO proj_man_sys.project_worker(project_id, worker_id)
VALUES (2, 1);
INSERT INTO proj_man_sys.project_worker(project_id, worker_id)
VALUES (3, 2);


INSERT INTO proj_man_sys.worker_task(worker_id, task_id)
VALUES (1, 1);
INSERT INTO proj_man_sys.worker_task(worker_id, task_id)
VALUES (1, 2);
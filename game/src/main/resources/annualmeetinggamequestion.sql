CREATE TABLE `annualmeetinggamequestion` (
  `id` int(11) NOT NULL,
  `topic` varchar(255) DEFAULT NULL,
  `answer_one` varchar(255) DEFAULT NULL,
  `answer_two` varchar(255) DEFAULT NULL,
  `answer_three` varchar(255) DEFAULT NULL,
  `answer_four` varchar(255) DEFAULT NULL,
  `right_answer` bit(64) DEFAULT NULL,
  `create_time` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
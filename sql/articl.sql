CREATE TABLE `shiro_article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `content` varchar(1000) NOT NULL,
  `user` varchar(100) not NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

--jdbc:mysql://120.79.143.100:3306/springbootdb?
--jdbc:mysql://{host::120.79.143.100}?[:{port::3306}][/{springbootdb}?][\?<&,user={root},password={hadoop200Cb.}>]
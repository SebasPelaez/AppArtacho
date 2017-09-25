﻿# CompuMovil_Lab1
LA BASE DE DATOS SE LLAMA lab3database

El script de creacion de tablas y demás es éste.

CREATE TABLE IF NOT EXISTS `user` (
`id` INT NOT NULL AUTO_INCREMENT, 
`username` VARCHAR(20) NOT NULL,
`password` VARCHAR(20) NOT NULL,
`name` VARCHAR(20) NOT NULL,
`last_name` VARCHAR(20) NOT NULL,
`gender` INT NOT NULL,
`birthday` VARCHAR(20) NOT NULL,
`phone` VARCHAR(20) NOT NULL, 
`address` VARCHAR(20) NOT NULL, 
`email` VARCHAR(20) NOT NULL, 
`city` VARCHAR(20) NOT NULL, 
`image` VARCHAR(20) NOT NULL, 
PRIMARY KEY( `id` )
) ENGINE=InnoDB  AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `apartament` (
`id` INT NOT NULL AUTO_INCREMENT, 
`name` VARCHAR(20) NOT NULL,
`type` VARCHAR(20) NOT NULL,
`value` INT NOT NULL,
`idUSer` INT NOT NULL,
`area` FLOAT NOT NULL,
`description` VARCHAR(20) NOT NULL,
`location` VARCHAR(20) NOT NULL, 
`numRooms` INT NOT NULL,  
PRIMARY KEY( `id` ),
FOREIGN KEY (`idUSer`) REFERENCES user(`id`)
) ENGINE=InnoDB  AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `resource` (
`id` INT NOT NULL AUTO_INCREMENT, 
`idApartment` INT NOT NULL,
`pathResource` VARCHAR(400) NOT NULL,
PRIMARY KEY( `id` ),
FOREIGN KEY (`idApartment`) REFERENCES apartament(`id`)
) ENGINE=InnoDB  AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

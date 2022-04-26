create schema if not exists `hotel_management`
default character set utf8mb4
default collate utf8mb4_bin;

use `hotel_management`;



-- ------------------------------------------------ --
--                  CREATE TABLE                    --
-- ------------------------------------------------ --

-- Table: user
create table if not exists `hotel_management`.`user` (
    `id` int not null auto_increment,
    `username` varchar(20) not null,
    `password` varchar(64) not null,
    `role` tinyint not null,
    `full_name` nvarchar(60) not null,
    `gender` tinyint not null,
    `year_of_birth` smallint not null,


    constraint PK_user primary key (`id`)
)
engine = InnoDB
default character set = utf8mb4
default collate = utf8mb4_bin;


-- Table: room
create table if not exists `hotel_management`.`room` (
    `id` int not null auto_increment,
    `name` nvarchar(50) not null,
    `description` text not null,
    `status` tinyint not null,
    `room_type_id` int,

    constraint PK_room primary key (`id`)
)
engine = InnoDB
default character set = utf8mb4
default collate = utf8mb4_bin;


-- Table: room_type
create table if not exists `hotel_management`.`room_type` (
    `id` int not null auto_increment,
    `name` nvarchar(50) not null,
    `price` int not null,

    constraint PK_room_type primary key (`id`)
)
engine = InnoDB
default character set = utf8mb4
default collate = utf8mb4_bin;


-- Table: rental_invoice
create table if not exists `hotel_management`.`rental_invoice` (
    `id` int not null auto_increment,
    `start_date` timestamp default current_timestamp,
    `room_id` int,
    `room_name` nvarchar(50),
    `customer_name` nvarchar(50) not null,
    `identity_number` VARCHAR(12) not null,
    `address` nvarchar(100) not null,
    `customer_type` int not null,

    constraint PK_rental_invoice primary key (`id`)
)
engine = InnoDB
default character set = utf8mb4
default collate = utf8mb4_bin;


-- Table: rental_receipt
create table if not exists `hotel_management`.`rental_receipt` (
    `id` int not null auto_increment,
    `rented_days` tinyint not null,
    `price` int not null,
    `total_price` int not null,
    `room_name` nvarchar(50),
    `room_id` int,

    constraint PK_rental_receipt primary key (`id`)
)
engine = InnoDB
default character set = utf8mb4
default collate = utf8mb4_bin;


-- Table: service
create table if not exists `hotel_management`.`service` (
    `id` INT not null auto_increment,
    `name` nvarchar(60) not null,
    `description` text not null,
    `price` int not null,
    `notes` text not null,

    constraint PK_service primary key (`id`)
)
engine = InnoDB
default character set = utf8mb4
default collate = utf8mb4_bin;


-- Table: service_invoice
create table if not exists `hotel_management`.`service_invoice` (
    `id` int not null auto_increment,
    `service_name` nvarchar(60),
    `number_of_customers` int not null,
    `total_price` int not null,
    `time_used` int not null,
    `notes` text not null,
    `room_id` int,
    `room_name` nvarchar(50),
    `service_id` int not null,

    constraint PK_service_invoice primary key (`id`)
)
engine = InnoDB
default character set = utf8mb4
default collate = utf8mb4_bin;


-- Table: product
create table if not exists `hotel_management`.`product` (
    `id` int not null auto_increment,
    `name` nvarchar(60) not null,
    `price` int not null,
    `stock` int not null,
    `notes` text not null,
    `product_type` int not null,

    constraint PK_product primary key (`id`)
)
engine = InnoDB
default character set = utf8mb4
default collate = utf8mb4_bin;


-- Table: receipt_detail
create table if not exists `hotel_management`.`receipt_detail` (
    `id` int not null auto_increment,
    `receipt_id` int not null,
    `quantity` tinyint not null,
    `product_name` nvarchar(60) not null,
    `product_type` int not null,
    `price` int not null,

    constraint PK_receipt_detail primary key (`id`)
)
engine = InnoDB
default character set = utf8mb4
default collate = utf8mb4_bin;

-- Table: receipt
create table if not exists `hotel_management`.`receipt` (
    `id` int not null auto_increment,
    `customer_name` nvarchar(50) not null,
    `purchased_date` timestamp default current_timestamp,
    `notes` text not null,
    `total_price` int not null,

    constraint PK_receipt primary key (`id`)
)
engine = InnoDB
default character set = utf8mb4
default collate = utf8mb4_bin;


-- Table: import_invoice_detail
create table if not exists `hotel_management`.`import_invoice_detail` (
    `id` int not null auto_increment,
    `import_invoice_id` int not null,
    `quantity` tinyint not null,
    `product_name` nvarchar(60) not null,
    `price` int not null,

    constraint PK_import_invoice_detail primary key (`id`)
)
engine = InnoDB
default character set = utf8mb4
default collate = utf8mb4_bin;

-- Table: import_invoice
create table if not exists `hotel_management`.`import_invoice` (
    `id` int not null auto_increment,
    `customer_name` nvarchar(50) not null,
    `imported_date` timestamp default current_timestamp,
    `notes` text not null,
    `total_price` int not null,

    constraint PK_import_invoice primary key (`id`)
)
engine = InnoDB
default character set = utf8mb4
default collate = utf8mb4_bin;



-- ------------------------------------------------ --
--              CREATE FOREIGN KEY                  --
-- ------------------------------------------------ --

-- Table: room
-- room(room_type_id) ==> room_type(id)
alter table `hotel_management`.`room`
add constraint FK_room_room_type
foreign key (`room_type_id`)
references `hotel_management`.`room_type`(`id`);


-- Table: rental_invoice
-- rental_invoice(room_id) ==> room(id)
alter table `hotel_management`.`rental_invoice`
add constraint FK_rental_invoice_room
foreign key (`room_id`)
references `hotel_management`.`room`(`id`);


-- Table: rental_receipt
-- rental_receipt(room_id) ==> room(id)
alter table `hotel_management`.`rental_receipt`
add constraint FK_rental_receipt_room
foreign key (`room_id`)
references `hotel_management`.`room`(`id`);


-- Table: service_invoice
-- service_invoice(service_id) ==> service(id)
alter table `hotel_management`.`service_invoice`
add constraint FK_service_invoice_service_type
foreign key (`service_id`)
references `hotel_management`.`service`(`id`);


-- Table: service_invoice
-- service_invoice(room_id) ==> room(id)
alter table `hotel_management`.`service_invoice`
add constraint FK_service_invoice_room
foreign key (`room_id`)
references `hotel_management`.`room`(`id`);


-- Table: receipt_detail
-- receipt_detail(receipt_id) ==> receipt(id)
alter table `hotel_management`.`receipt_detail`
add constraint FK_receipt_detail_receipt
foreign key (receipt_id)
references `hotel_management`.`receipt`(id);


-- Table: import_invoice_detail
-- import_invoice_detail(import_invoice_id) ==> import_invoice(id)
alter table `hotel_management`.`import_invoice_detail`
add constraint FK_import_invoice_detail_import_invoice
foreign key (`import_invoice_id`)
references `hotel_management`.`import_invoice`(`id`);

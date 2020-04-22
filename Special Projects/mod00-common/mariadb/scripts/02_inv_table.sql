create table inventory (
  sku           char(10)     not null,
  description   char(30),
  location      char(6),
  loc_type      char(1),
  qty           integer,
  primary key (sku, location)
);

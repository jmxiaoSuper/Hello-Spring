-- Create table
create table TTRD_DEMO
(
  id          NUMBER,
  name        VARCHAR2(200),
  create_time VARCHAR2(21)
)
tablespace XIR_TRD_DAT
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the columns
comment on column TTRD_DEMO.id
  is '主键';
comment on column TTRD_DEMO.name
  is '名称';
comment on column TTRD_DEMO.create_time
  is '创建时间';
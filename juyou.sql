SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE IF NOT EXISTS `juyou_sys_tenants` (
  `tenants_id` varchar(64) NOT NULL COMMENT '租户ID',
  `name` varchar(128) NOT NULL COMMENT '名称',
  `status` tinyint DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`tenants_id`),
  KEY `idx_tenants_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='租户表';

CREATE TABLE IF NOT EXISTS `juyou_sys_mac` (
  `mac_id` varchar(64) NOT NULL COMMENT '主键',
  `parent_id` varchar(64) DEFAULT NULL COMMENT '父机构主键',
  `code` varchar(64) DEFAULT NULL COMMENT '编码',
  `name` varchar(128) DEFAULT NULL COMMENT '名称',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `status` int DEFAULT 1 COMMENT '状态',
  `type` int DEFAULT NULL COMMENT '机构类型',
  `sequence` int DEFAULT 0 COMMENT '排序号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_name` varchar(64) DEFAULT NULL COMMENT '创建人',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `last_update_name` varchar(64) DEFAULT NULL COMMENT '最后修改人',
  `tenants_id` varchar(64) DEFAULT NULL COMMENT '租户Id',
  PRIMARY KEY (`mac_id`),
  KEY `idx_mac_parent` (`parent_id`),
  KEY `idx_mac_tenants` (`tenants_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='机构表';

CREATE TABLE IF NOT EXISTS `juyou_sys_user` (
  `user_id` varchar(64) NOT NULL COMMENT '用户主键',
  `code` varchar(64) DEFAULT NULL COMMENT '编码',
  `name` varchar(128) DEFAULT NULL COMMENT '用户名',
  `user_type` int DEFAULT 0 COMMENT '用户类型',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `secret` varchar(128) DEFAULT NULL COMMENT '加密盐',
  `status` int DEFAULT 1 COMMENT '状态 0禁用,1启用',
  `lock_status` int DEFAULT 0 COMMENT '锁定状态 0正常,1锁定',
  `lock_time` int DEFAULT 0 COMMENT '锁定时间',
  `del_flag` int DEFAULT 0 COMMENT '是否删除0正常,1删除',
  `mac_id` varchar(64) DEFAULT NULL COMMENT '数据归属机构编码',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `login_ip` varchar(64) DEFAULT NULL COMMENT '登录IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `collect_num` varchar(32) DEFAULT NULL COMMENT '文章收藏数量',
  `sys_type` int DEFAULT 0 COMMENT '类型0默认,1预留',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_name` varchar(64) DEFAULT NULL COMMENT '创建人',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `last_update_name` varchar(64) DEFAULT NULL COMMENT '最后修改人',
  `tenants_id` varchar(64) DEFAULT NULL COMMENT '租户Id',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_user_code_tenant` (`code`, `tenants_id`),
  KEY `idx_user_tenants` (`tenants_id`),
  KEY `idx_user_mac` (`mac_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `juyou_sys_role` (
  `role_id` varchar(64) NOT NULL COMMENT '主键',
  `name` varchar(128) DEFAULT NULL COMMENT '角色名称',
  `code` varchar(64) DEFAULT NULL COMMENT '角色编号admin,user',
  `status` int DEFAULT 1 COMMENT '状态',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `sys_type` int DEFAULT 0 COMMENT '角色类型',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_name` varchar(64) DEFAULT NULL COMMENT '创建人',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `last_update_name` varchar(64) DEFAULT NULL COMMENT '最后修改人',
  `tenants_id` varchar(64) DEFAULT NULL COMMENT '租户Id',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `uk_role_code_tenant` (`code`, `tenants_id`),
  KEY `idx_role_tenants` (`tenants_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';

CREATE TABLE IF NOT EXISTS `juyou_sys_permission` (
  `permission_id` varchar(64) NOT NULL COMMENT '系统菜单主键',
  `code` varchar(128) DEFAULT NULL COMMENT '编码',
  `name` varchar(128) DEFAULT NULL COMMENT '名称',
  `status` int DEFAULT 1 COMMENT '状态',
  `parent_id` varchar(64) DEFAULT NULL COMMENT '父ID',
  `url` varchar(500) DEFAULT NULL COMMENT '路径',
  `auth_url` varchar(500) DEFAULT NULL COMMENT '权限标识',
  `component_name` varchar(255) DEFAULT NULL COMMENT '前端组件名称',
  `component_url` varchar(500) DEFAULT NULL COMMENT '前端组件路径',
  `type` int DEFAULT 0 COMMENT '0一级菜单;1子菜单;2按钮权限',
  `icon` varchar(128) DEFAULT NULL COMMENT '菜单图标',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `sequence` int DEFAULT 0 COMMENT '排序号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_name` varchar(64) DEFAULT NULL COMMENT '创建人',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `last_update_name` varchar(64) DEFAULT NULL COMMENT '最后修改人',
  `sys_type` int DEFAULT 0 COMMENT '类型，0默认,-1超管',
  PRIMARY KEY (`permission_id`),
  KEY `idx_permission_parent` (`parent_id`),
  KEY `idx_permission_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单权限表';

CREATE TABLE IF NOT EXISTS `juyou_sys_user_role` (
  `user_role_id` varchar(64) NOT NULL COMMENT '用户角色主键',
  `user_id` varchar(64) NOT NULL COMMENT '用户主键',
  `role_id` varchar(64) NOT NULL COMMENT '角色主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_name` varchar(64) DEFAULT NULL COMMENT '创建人',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `last_update_name` varchar(64) DEFAULT NULL COMMENT '最后修改人',
  `tenants_id` varchar(64) DEFAULT NULL COMMENT '租户Id',
  PRIMARY KEY (`user_role_id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`, `tenants_id`),
  KEY `idx_user_role_tenants` (`tenants_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关系表';

CREATE TABLE IF NOT EXISTS `juyou_sys_role_permission` (
  `role_permission_id` varchar(64) NOT NULL COMMENT '角色菜单主键',
  `permission_id` varchar(64) NOT NULL COMMENT '权限主键',
  `role_id` varchar(64) NOT NULL COMMENT '角色主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_name` varchar(64) DEFAULT NULL COMMENT '创建人',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `last_update_name` varchar(64) DEFAULT NULL COMMENT '最后修改人',
  `tenants_id` varchar(64) DEFAULT NULL COMMENT '租户Id',
  PRIMARY KEY (`role_permission_id`),
  UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`, `tenants_id`),
  KEY `idx_role_permission_tenants` (`tenants_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色权限关系表';

CREATE TABLE IF NOT EXISTS `juyou_sys_dict` (
  `dict_id` varchar(64) NOT NULL COMMENT '系统字典主键',
  `code` varchar(64) DEFAULT NULL COMMENT '编码',
  `name` varchar(128) DEFAULT NULL COMMENT '名称',
  `status` int DEFAULT 1 COMMENT '状态',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `sequence` int DEFAULT 0 COMMENT '排序号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_name` varchar(64) DEFAULT NULL COMMENT '创建人',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `last_update_name` varchar(64) DEFAULT NULL COMMENT '最后修改人',
  `tenants_id` varchar(64) DEFAULT NULL COMMENT '租户Id',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `uk_dict_code_tenant` (`code`, `tenants_id`),
  KEY `idx_dict_tenants` (`tenants_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统字典表';

CREATE TABLE IF NOT EXISTS `juyou_sys_dict_detail` (
  `dict_detail_id` varchar(64) NOT NULL COMMENT '系统字典明细主键',
  `dict_id` varchar(64) NOT NULL COMMENT '系统字典主键',
  `name` varchar(128) DEFAULT NULL COMMENT '名称',
  `value` varchar(255) DEFAULT NULL COMMENT '值',
  `status` int DEFAULT 1 COMMENT '状态',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `sequence` int DEFAULT 0 COMMENT '排序号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_name` varchar(64) DEFAULT NULL COMMENT '创建人',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `last_update_name` varchar(64) DEFAULT NULL COMMENT '最后修改人',
  `tenants_id` varchar(64) DEFAULT NULL COMMENT '租户Id',
  PRIMARY KEY (`dict_detail_id`),
  KEY `idx_dict_detail_dict` (`dict_id`),
  KEY `idx_dict_detail_tenants` (`tenants_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统字典明细表';

CREATE TABLE IF NOT EXISTS `juyou_sys_config` (
  `config_id` varchar(64) NOT NULL COMMENT '系统参数主键',
  `code` varchar(64) DEFAULT NULL COMMENT '编码',
  `name` varchar(128) DEFAULT NULL COMMENT '名称',
  `value` varchar(1000) DEFAULT NULL COMMENT '值',
  `status` int DEFAULT 1 COMMENT '状态',
  `sequence` int DEFAULT 0 COMMENT '排序号',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `sys_type` varchar(64) DEFAULT NULL COMMENT '系统类型',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_name` varchar(64) DEFAULT NULL COMMENT '创建人',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `last_update_name` varchar(64) DEFAULT NULL COMMENT '最后修改人',
  `tenants_id` varchar(64) DEFAULT NULL COMMENT '租户Id',
  PRIMARY KEY (`config_id`),
  UNIQUE KEY `uk_config_code_tenant` (`code`, `tenants_id`),
  KEY `idx_config_tenants` (`tenants_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统参数配置表';

CREATE TABLE IF NOT EXISTS `juyou_cms_info_type` (
  `type_id` varchar(64) NOT NULL COMMENT '分类ID',
  `name` varchar(128) DEFAULT NULL COMMENT '分类名称',
  `parent_id` varchar(64) DEFAULT NULL COMMENT '父分类ID',
  `code` varchar(64) DEFAULT NULL COMMENT '编码',
  `sequence` int DEFAULT 0 COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_name` varchar(64) DEFAULT NULL COMMENT '创建人',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `last_update_name` varchar(64) DEFAULT NULL COMMENT '最后修改人',
  `tenants_id` varchar(64) DEFAULT NULL COMMENT '租户Id',
  PRIMARY KEY (`type_id`),
  KEY `idx_info_type_tenants` (`tenants_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文章分类表';

CREATE TABLE IF NOT EXISTS `juyou_cms_info` (
  `info_id` varchar(64) NOT NULL COMMENT '文章ID',
  `name` varchar(255) DEFAULT NULL COMMENT '文章名',
  `big_type` varchar(128) DEFAULT NULL COMMENT '大类',
  `brief_introduction` varchar(1000) DEFAULT NULL COMMENT '简介',
  `cover` varchar(500) DEFAULT NULL COMMENT '封面',
  `content` longtext COMMENT '内容',
  `type_id` varchar(64) DEFAULT NULL COMMENT '分类ID',
  `hot` int DEFAULT 0 COMMENT '是否热门',
  `recommended` int DEFAULT 0 COMMENT '是否推荐',
  `latest` int DEFAULT 0 COMMENT '是否最新',
  `curation` int DEFAULT 0 COMMENT '是否精选',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_name` varchar(64) DEFAULT NULL COMMENT '创建人',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `last_update_name` varchar(64) DEFAULT NULL COMMENT '最后修改人',
  `tenants_id` varchar(64) DEFAULT NULL COMMENT '租户Id',
  PRIMARY KEY (`info_id`),
  KEY `idx_info_type` (`type_id`),
  KEY `idx_info_tenants` (`tenants_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文章表';

CREATE TABLE IF NOT EXISTS `juyou_cms_type_attributes` (
  `attr_id` varchar(64) NOT NULL COMMENT '属性ID',
  `name` varchar(128) DEFAULT NULL COMMENT '属性名',
  `val_type` varchar(64) DEFAULT NULL COMMENT '值类型',
  `order_num` int DEFAULT 0 COMMENT '序号',
  `is_required` int DEFAULT 0 COMMENT '是否必填',
  `is_dict` int DEFAULT 0 COMMENT '是否字典',
  `dict_key` varchar(128) DEFAULT NULL COMMENT '字典key',
  `type_id` varchar(64) DEFAULT NULL COMMENT '分类ID',
  `code` varchar(64) DEFAULT NULL COMMENT '标识code',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_name` varchar(64) DEFAULT NULL COMMENT '创建人',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `last_update_name` varchar(64) DEFAULT NULL COMMENT '最后修改人',
  `tenants_id` varchar(64) DEFAULT NULL COMMENT '租户Id',
  PRIMARY KEY (`attr_id`),
  KEY `idx_type_attr_type` (`type_id`),
  KEY `idx_type_attr_tenants` (`tenants_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文章分类属性表';

CREATE TABLE IF NOT EXISTS `juyou_cms_type_attr_val` (
  `attr_id` varchar(64) NOT NULL COMMENT '主键',
  `info_id` varchar(64) DEFAULT NULL COMMENT '文章ID',
  `type_id` varchar(64) DEFAULT NULL COMMENT '分类ID',
  `val` varchar(2000) DEFAULT NULL COMMENT '属性值',
  `type_attr_id` varchar(64) DEFAULT NULL COMMENT '分类属性ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_name` varchar(64) DEFAULT NULL COMMENT '创建人',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `last_update_name` varchar(64) DEFAULT NULL COMMENT '最后修改人',
  `tenants_id` varchar(64) DEFAULT NULL COMMENT '租户Id',
  PRIMARY KEY (`attr_id`),
  KEY `idx_attr_val_info` (`info_id`),
  KEY `idx_attr_val_type` (`type_id`),
  KEY `idx_attr_val_tenants` (`tenants_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文章分类属性值表';

CREATE TABLE IF NOT EXISTS `juyou_sys_table_info` (
  `table_name` varchar(128) NOT NULL COMMENT '表名',
  `column_name` varchar(128) NOT NULL COMMENT '字段名',
  `java_type` varchar(128) DEFAULT NULL COMMENT 'Java类型',
  `java_name` varchar(128) DEFAULT NULL COMMENT 'Java字段名',
  `add_columns` int DEFAULT 0 COMMENT '新增',
  `edit_columns` int DEFAULT 0 COMMENT '编辑',
  `list_columns` int DEFAULT 0 COMMENT '列表',
  `query_columns` int DEFAULT 0 COMMENT '查询',
  `query_type` int DEFAULT 0 COMMENT '查询方式',
  `not_null_columns` int DEFAULT 0 COMMENT '必填',
  `out_type` int DEFAULT 0 COMMENT '输入框类型',
  `ditc_type` varchar(128) DEFAULT NULL COMMENT '字典类型',
  `reg_exp` varchar(500) DEFAULT NULL COMMENT '正则表达式',
  `reg_msg` varchar(500) DEFAULT NULL COMMENT '正则验证消息',
  `module_name` varchar(128) DEFAULT NULL COMMENT '上次生成moduleName',
  `field_desc` varchar(255) DEFAULT NULL COMMENT '字段说明',
  PRIMARY KEY (`table_name`, `column_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码生成字段配置表';

SET FOREIGN_KEY_CHECKS = 1;

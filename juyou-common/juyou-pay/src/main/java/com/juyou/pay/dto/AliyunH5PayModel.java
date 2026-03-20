package com.juyou.pay.dto;

import lombok.Data;

@Data
public class AliyunH5PayModel {

    private String subject;

    private String out_trade_no;

    private String total_amount;

    private String seller_id;

    private String quit_url;

    private String product_code;

    private String body;

    private String timeout_express;

    private String time_expire;

    private String auth_token;

    private String goods_type;

    private String passback_params;

    private String promo_params;

    private String royalty_info;

    private String extend_params;

    private String sub_merchant;

    private String merchant_order_no;

    private String enable_pay_channels;

    private String disable_pay_channels;

    private String store_id;

    private String goods_detail;

    private String settle_info;

    private String invoice_info;

    private String specified_channel;

    private String business_params;

    private String ext_user_info;

}

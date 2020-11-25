package com.google.mgmg22demo.api

import com.google.mgmg22.lib_http.retrofit

/**
 * @Description:
 * @Author:         mgmg22
 * @CreateDate:     2019-12-06 11:30
 */
interface Api {

//    /***********************购物车**********************************/

//    /**
//     * 加入购物车
//     */
//    @POST("api/cart/buy")
//    fun cartBuy(@Body req: CartBuyReq): Observable<BaseResult<CartListBean>>

}

object DemoApi : Api by retrofit.create(Api::class.java)



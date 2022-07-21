package com.bytes.app.ui.home

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ProductListResponse {

    @JsonProperty("hits")
    val vendors: ArrayList<Product>? = null

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class VendorInventory: Serializable{
    @JsonProperty("price")
    var productPrice: Double? = 0.0

    @JsonProperty("list_price")
    var listPrice: Double? = 0.0
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Product : Serializable {
    @JsonProperty("name")
    var product_Name: String? = null

    @JsonProperty("description")
    var product_Description: String? = null

    @JsonProperty("main_image")
    var product_Image: String? = null

    @JsonProperty("id")
    var id: String? = null

    @JsonProperty("is_favourite_product")
    var is_product_Favourite: Boolean = false

    var isLikeView = true

    @JsonProperty("vendor_inventory")
    val vendorsInventory: ArrayList<VendorInventory>? = null

}
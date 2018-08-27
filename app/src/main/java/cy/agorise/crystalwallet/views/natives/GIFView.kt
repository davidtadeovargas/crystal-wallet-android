package cy.agorise.crystalwallet.views.natives

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import cy.agorise.crystalwallet.R


/*
* GIF implementation based on existing imageview class
* */
class GIFView : ImageView {

    /*
    * Contains aditional options for the gif
    * */
    private var options: RequestOptions? = RequestOptions()




    constructor(context:Context,attrs: AttributeSet?) : super(context,attrs) {
    }

    /*
    * Load the gif
    * */
    fun load(rawID:Int){
        Glide.with(this).asGif().load(rawID).apply(options!!).into(this)
    }

    /*
    * Option to fit the gif in the container
    * */
    fun centerCrop(){
        options!!.centerCrop()
    }
}
package cy.agorise.crystalwallet.views.natives

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.widget.VideoView
import cy.agorise.crystalwallet.R
import kotlinx.android.synthetic.main.empty_activity.*


/*
* Extensión for videoview
* */
class VideoExView(context: Context?, attrs: AttributeSet?) : VideoView(context, attrs) {

    /*
    * Set the path based on raw, this should be called first to set the video path
    * */
    fun setVideoRaw(rawID:Int){
        val uriPath = "android.resource://" + context.packageName + "/" + R.raw.appbar_background
        val uri = Uri.parse(uriPath)
        this.setVideoURI(uri)
    }


    /*
    * With this method the video play continues
    * */
    fun playContinius(){

        start()
        this.setOnCompletionListener {
            start()
        }
    }
}
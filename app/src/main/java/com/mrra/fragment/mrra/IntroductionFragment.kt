package com.mrra.fragment.mrra

import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.MediaController
import android.widget.ScrollView
import android.widget.TextView
import com.mrra.R
import com.mrra.base.BaseFragment
import com.mrra.utils.getRawUri
import com.mrra.weight.VideoViewWithListener

class IntroductionFragment : BaseFragment() {

    private lateinit var root: ScrollView
    private lateinit var videoView: VideoViewWithListener
    private lateinit var tip: TextView

    private lateinit var mediaController: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_introduction, container, false).apply {
            root = findViewById(R.id.sv_introduction_root)
            videoView = findViewById(R.id.vv_introduction_video)
            tip = findViewById(R.id.tv_introduction_tip)

            with(videoView) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE)
                }
                mediaController = MediaController(requireContext())
                mediaController.setAnchorView(this)
                setMediaController(mediaController)
                setVideoURI(getRawUri(R.raw.video_introduction))
                setOnCompletionListener {
                    tip.visibility = View.VISIBLE
                }
                setStartListener {
                    tip.visibility = View.GONE
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                root.setOnScrollChangeListener { _, _, _, _, _ ->
                    mediaController.hide()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mediaController.hide()
    }

    companion object {
        @JvmStatic
        fun newInstance() = IntroductionFragment()
    }
}
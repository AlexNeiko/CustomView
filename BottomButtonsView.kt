package com.dzensport.customview_100821.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout

enum class BottomButtonsAction {
    POSITIVE, NEGATIVE
}

typealias OnBottomButtonsActionListener = (BottomButtonsAction) -> Unit

class BottomButtonsView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: PartButtonsBinding
    private var listener : OnBottomButtonsActionListener? = null
    var isProgressMode: Boolean = false
        get() =  field
    set(value) {
        if(value) {
            binding.buttonPositive.visibility = INVISIBLE
            binding.buttonNegative.visibility = INVISIBLE
            binding.progressBar.visibility = VISIBLE
        } else {
            binding.buttonPositive.visibility = VISIBLE
            binding.buttonNegative.visibility = VISIBLE
            binding.progressBar.visibility = GONE
        }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, R.style.AppDefaultBottomButtonsStyle)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.bottomButtonStyle)
    constructor(context: Context) : this(context, null)

    init {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.part_buttons, this, true)

        binding = PartButtonsBinding.bind(this)

        initializeAttributes(attrs, defStyleAttr, defStyleRes)

        initListeners()
    }

    private fun initializeAttributes (attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int ) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.BottomButtonsView,
            defStyleAttr,
            defStyleRes)

        setPositiveButtonText("Apply")

        setNegativeButtonText("Cancel")

        val positiveButtonBgColor : Int = typedArray.getColor(R.styleable.BottomButtonsView_bottomPositiveButtonBackgroundColor, Color.BLACK)
        binding.buttonPositive.backgroundTintList = ColorStateList.valueOf(positiveButtonBgColor)

        val negativeButtonBgColor : Int = typedArray.getColor(R.styleable.BottomButtonsView_bottomNegativeButtonBackgroundColor, Color.WHITE)
        binding.buttonNegative.backgroundTintList = ColorStateList.valueOf(negativeButtonBgColor)

        this@BottomButtonsView.isProgressMode  = typedArray.getBoolean(R.styleable.BottomButtonsView_bottomProgressMode, false)

        typedArray.recycle()

    }


    private fun initListeners() {
        binding.buttonPositive.setOnClickListener {
            this.listener?.invoke(BottomButtonsAction.POSITIVE)
        }

        binding.buttonNegative.setOnClickListener {
            this.listener?.invoke(BottomButtonsAction.NEGATIVE)
        }
    }
    
    fun setListener(listener : OnBottomButtonsActionListener) {
        this.listener = listener
    }

    fun setPositiveButtonText(text : String?) {
        binding.buttonPositive.text = text
    }

    fun setNegativeButtonText(text : String?) {
        binding.buttonNegative.text = text
    }


    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()!!
        val savedState = SavedState(superState)
        savedState.positiveButtonText = binding.buttonPositive.text.toString()
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState) 
        binding.buttonPositive.text = savedState.positiveButtonText
    }



   class SavedState : BaseSavedState {

        var positiveButtonText: String? = null

        constructor(superState: Parcelable) : super(superState)
        constructor(parcel: Parcel) : super(parcel) {

            positiveButtonText = parcel.readString()
      
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(positiveButtonText)
        }

        companion object {
            @JvmField
            var CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> { 
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                   return Array(size) { null }
                }
            }
        }
    }
}

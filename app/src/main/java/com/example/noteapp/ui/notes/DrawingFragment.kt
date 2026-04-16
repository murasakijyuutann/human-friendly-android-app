package com.example.noteapp.ui.notes

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentDrawingBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class DrawingFragment : Fragment() {

    private var _binding: FragmentDrawingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrawingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ツールバーの戻るボタン
        binding.toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // 色選択 + ロングプレスで色名をToastで表示
        val colorViews = listOf(
            binding.colorBlack to Pair(Color.BLACK,                getString(R.string.color_black)),
            binding.colorRed   to Pair(Color.parseColor("#E53935"), getString(R.string.color_red)),
            binding.colorBlue  to Pair(Color.parseColor("#1E88E5"), getString(R.string.color_blue)),
            binding.colorWhite to Pair(Color.WHITE,                 getString(R.string.color_white))
        )
        colorViews.forEach { (colorView, pair) ->
            val (color, name) = pair
            colorView.setOnClickListener { binding.drawingView.setColor(color) }
            colorView.setOnLongClickListener {
                // カラーアイコンの画面上の位置を取得してラベルをその上に表示する
                val location = IntArray(2)
                colorView.getLocationInWindow(location)
                val rootLocation = IntArray(2)
                binding.root.getLocationInWindow(rootLocation)

                val label = binding.colorNameLabel
                label.text = name
                label.visibility = View.VISIBLE

                // ラベルはアイコンの中央上に配置（表示後にサイズが確定してから調整）
                label.post {
                    val x = (location[0] - rootLocation[0]) + colorView.width / 2f - label.width / 2f
                    val y = (location[1] - rootLocation[1]) - label.height - 4f
                    label.x = x.coerceAtLeast(0f)
                    label.y = y.coerceAtLeast(0f)
                }

                // 1.5秒後に非表示
                Handler(Looper.getMainLooper()).postDelayed({
                    label.visibility = View.GONE
                }, 1500)
                true
            }
        }

        // クリア
        binding.btnClear.setOnClickListener {
            binding.drawingView.clear()
        }

        // 保存（内部ストレージにPNGとして保存）
        binding.btnSave.setOnClickListener {
            saveDrawing()
        }
    }

    private fun saveDrawing() {
        val bitmap = binding.drawingView.getBitmap()
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "drawing_$timestamp.png"
        val file = File(requireContext().filesDir, fileName)
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            Snackbar.make(binding.root, "Saved: $fileName", Snackbar.LENGTH_LONG).show()
        } catch (e: Exception) {
            Snackbar.make(binding.root, "Save failed: ${e.message}", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


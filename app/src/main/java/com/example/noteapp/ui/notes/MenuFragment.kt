package com.example.noteapp.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // "Notes" カードをタップするとノートリスト画面へ遷移
        view.findViewById<View>(R.id.cardNotes).setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToNoteListFragment()
            findNavController().navigate(action)
        }

        // "Drawing" カードをタップすると描画画面へ遷移
        view.findViewById<View>(R.id.cardDrawing).setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToDrawingFragment()
            findNavController().navigate(action)
        }
    }
}


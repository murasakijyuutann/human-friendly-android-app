package com.example.noteapp.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteapp.R
import com.example.noteapp.data.model.Note
import com.example.noteapp.databinding.FragmentNoteEditBinding

class NoteEditFragment : Fragment() {

    private var _binding: FragmentNoteEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteEditViewModel by viewModels()
    private val args: NoteEditFragmentArgs by navArgs()

    private var currentNote: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteId = args.noteId
        val isEditing = noteId != -1

        // Load existing note if editing
        if (isEditing) {
            viewModel.loadNote(noteId.toLong())
        }

        binding.btnDelete.visibility = if (isEditing) View.VISIBLE else View.GONE

        viewModel.note.observe(viewLifecycleOwner) { note ->
            currentNote = note
            note?.let {
                binding.etTitle.setText(it.title)
                binding.etBody.setText(it.body)
            }
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val body = binding.etBody.text.toString().trim()
            if (title.isEmpty()) {
                binding.tilTitle.error = getString(R.string.title_required)
                return@setOnClickListener
            }
            binding.tilTitle.error = null
            viewModel.saveNote(title, body, currentNote)
            findNavController().navigateUp()
        }

        binding.btnDelete.setOnClickListener {
            currentNote?.let { note ->
                viewModel.deleteNote(note)
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




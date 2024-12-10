package com.example.heartogether.data.network

data class ResponseMisPronun (
    var sentence : String,
    var ipaSentence : String,
    var ipaUser : String ?= ""
)

data class ResponsePostRequest (
    val ipa_transcript: String,//phat am minh noi
    val pronunciation_accuracy: String,//diem
    val real_transcripts: String,//sentence
    val real_transcripts_ipa: String,//ipa sentence
    val is_letter_correct_all_words: String//0101
)
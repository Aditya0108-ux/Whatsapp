package com.adityaa0108.whatsappclone.model

import java.sql.Timestamp

data class MessageModel(
    var message: String?= "",
    var senderId : String?= "",
    var timeStamp:  Long ?= 0

)

package com.tatq

// Payloads take from https://core.telegram.org/bots/webhooks

fun normalMessage(): String {
    return """
    {
    "update_id":10000,
    "message":{
      "date":1441645532,
      "chat":{
         "last_name":"Test Lastname",
         "id":1111111,
         "type": "private",
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "message_id":1365,
      "from":{
         "last_name":"Test Lastname",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "text":"/start"
    }
    }
    """.trimIndent()
}

fun forwardedMessage(): String {
    return """
    {
    "update_id":10000,
    "message":{
      "date":1441645532,
      "chat":{
         "last_name":"Test Lastname",
         "id":1111111,
         "type": "private",
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "message_id":1365,
      "from":{
         "last_name":"Test Lastname",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "forward_from": {
         "last_name":"Forward Lastname",
         "id": 222222,
         "first_name":"Forward Firstname"
      },
      "forward_date":1441645550,
      "text":"/start"
    }
    }
    """.trimIndent()
}

fun forwardedChannelMessage(): String {
    return """
    {
    "update_id":10000,
    "message":{
      "date":1441645532,
      "chat":{
         "last_name":"Test Lastname",
         "type": "private",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "message_id":1365,
      "from":{
         "last_name":"Test Lastname",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "forward_from": {
         "id": -10000000000,
         "type": "channel",
         "title": "Test channel"
      },
      "forward_date":1441645550,
      "text":"/start"
    }
    }
    """.trimIndent()
}

fun messageWithReply(): String {
    return """
    {
    "update_id":10000,
    "message":{
      "date":1441645532,
      "chat":{
         "last_name":"Test Lastname",
         "type": "private",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "message_id":1365,
      "from":{
         "last_name":"Test Lastname",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "text":"/start",
      "reply_to_message":{
          "date":1441645000,
          "chat":{
              "last_name":"Reply Lastname",
              "type": "private",
              "id":1111112,
              "first_name":"Reply Firstname",
              "username":"Testusername"
          },
          "message_id":1334,
          "text":"Original"
      }
    }
    }
    """.trimIndent()
}

fun editedMessage(): String {
    return """
    {
    "update_id":10000,
    "edited_message":{
      "date":1441645532,
      "chat":{
         "last_name":"Test Lastname",
         "type": "private",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "message_id":1365,
      "from":{
         "last_name":"Test Lastname",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "text":"Edited text",
      "edit_date": 1441646600
    }
    }
    """.trimIndent()
}

fun messageWithEntities(): String {
    return """
    {
    "update_id":10000,
    "message":{
      "date":1441645532,
      "chat":{
         "last_name":"Test Lastname",
         "type": "private",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "message_id":1365,
      "from":{
         "last_name":"Test Lastname",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "text":"Bold and italics",
      "entities": [
          {
              "type": "italic",
              "offset": 9,
              "length": 7
          },
          {
              "type": "bold",
              "offset": 0,
              "length": 4
          }
          ]
    }
    }
    """.trimIndent()
}

fun messageWithAudio(): String {
    return """
    {
    "update_id":10000,
    "message":{
      "date":1441645532,
      "chat":{
         "last_name":"Test Lastname",
         "type": "private",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "message_id":1365,
      "from":{
         "last_name":"Test Lastname",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "audio": {
          "file_id": "AwADBAADbXXXXXXXXXXXGBdhD2l6_XX",
          "duration": 243,
          "mime_type": "audio/mpeg",
          "file_size": 3897500,
          "title": "Test music file"
      }
    }
    }
    """.trimIndent()
}

fun voiceMessage(): String {
    return """
    {
    "update_id":10000,
    "message":{
      "date":1441645532,
      "chat":{
         "last_name":"Test Lastname",
         "type": "private",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "message_id":1365,
      "from":{
         "last_name":"Test Lastname",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "voice": {
          "file_id": "AwADBAADbXXXXXXXXXXXGBdhD2l6_XX",
          "duration": 5,
          "mime_type": "audio/ogg",
          "file_size": 23000
      }
    }
    }
    """.trimIndent()
}

fun messageWithDocument(): String {
    return """
    {
    "update_id":10000,
    "message":{
      "date":1441645532,
      "chat":{
         "last_name":"Test Lastname",
         "type": "private",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "message_id":1365,
      "from":{
         "last_name":"Test Lastname",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "document": {
          "file_id": "AwADBAADbXXXXXXXXXXXGBdhD2l6_XX",
          "file_name": "Testfile.pdf",
          "mime_type": "application/pdf",
          "file_size": 536392
      }
    }
    }
    """.trimIndent()
}

fun inlineQuery(): String {
    return """
    {
    "update_id":10000,
    "inline_query":{
      "id": 134567890097,
      "from":{
         "last_name":"Test Lastname",
         "type": "private",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "query": "inline query",
      "offset": ""
    }
    }
    """.trimIndent()
}

fun chosenInlineQuery(): String {
    return """
    {
    "update_id":10000,
    "chosen_inline_result":{
      "result_id": "12",
      "from":{
         "last_name":"Test Lastname",
         "type": "private",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "query": "inline query",
      "inline_message_id": "1234csdbsk4839"
    }
    }
    """.trimIndent()
}

fun callbackQuery(): String {
    return """
    {
    "update_id":10000,
    "callback_query":{
      "id": "4382bfdwdsb323b2d9",
      "from":{
         "last_name":"Test Lastname",
         "type": "private",
         "id":1111111,
         "first_name":"Test Firstname",
         "username":"Testusername"
      },
      "data": "Data from button callback",
      "inline_message_id": "1234csdbsk4839"
    }
    }
    """.trimIndent()
}

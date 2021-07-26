# CarPoolRoutePlanner
Kyle Morrison

Solomon Loche

Will Duncan

Leron Prout

Landon Rawson

Manish Kadayat


# Use this to connect to the server for anything needed from the backend
lifecycleScope.launch {
    val client = HttpClient()
    val httpResponse: HttpResponse = client.get("{IP and/or path}")
    val byteArrayBody: ByteArray = httpResponse.receive()
    client.close()

    tv.text = byteArrayBody.decodeToString() # if you want the response decoded to a string
}


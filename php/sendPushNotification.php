<?php


if($_SERVER['REQUEST_METHOD']=='POST'){
	$title = $_POST['title'];
	$message = $_POST['message'];
	$data = $_POST['data'];
	$topic = $_POST['topic'];
}

// $data = "";
// $data = strip_tags($_POST['data']);
// $data = json_decode($data, true);


echo sendPushNotification($title, $message, $data, $topic);

function sendPushNotification($title, $message, $data, $topic){

echo $data."  ".$title;

  $topicName = $topic; //$data['topicname'];

  define( "API_ACCESS_KEY", "AAAAPm7r2IY:APA91bFJytp8wZIPiU8sS1QRVUV8DOD2xawurTnDh3PufTpYGVKbzLAHkD5ri2Hhcol6lcXPmgS_5WtecegitY0OSX-KhHp4aJ4yyUlhogGftActz1IRK_xCTCgiPHiJazHzak-YJltp");

	$registrationIds = array();
	$fields = array
	(
        'to'  => '/topics/'.$topicName,
		    'data' => [
                "message" => "message at ".date('H:i:s'),
                "title" => "notification from php server",
                "timestamp" => time()
            ]
	);

	$headers = array
	(
		'Authorization: key=' . API_ACCESS_KEY,
		//'Authorization: key=AAAAlj2PVzE:APA91bEmS6VVrHw7OXKRYfyX4Ae-WJn0hm7ehkYKEPGV9y-z1blKYafHFZ7t-NPmJGV6yhWeC3O1qEPf0OYLyMkX7FoJCqJAyzR37K5sgaiL9geIfVV7EtvnbJ8HEv9NIG06bc_2jng6',
		'Content-Type: application/json'
	);

	$ch = curl_init();
	curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' );
	curl_setopt( $ch,CURLOPT_POST, true );
	curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
	curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
	curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
	//curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
	curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
	$result = curl_exec($ch );
	$error_msg = "NO ERROR";
	if (curl_error($ch)) {
        $error_msg = curl_error($ch);
    }
	curl_close( $ch );
	return "OK";
}

 ?>

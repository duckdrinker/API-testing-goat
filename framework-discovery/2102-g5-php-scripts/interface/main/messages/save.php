<?php
require_once __DIR__ . '/../../globals.php';
$title = $_POST['title'] ?? '';
sqlStatement("INSERT INTO messages (title) VALUES (?)", [$title]);
echo "saved";

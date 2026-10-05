<?php
require_once __DIR__ . '/../globals.php';
$list = $_GET['list_id'] ?? '';
if (!AclMain::aclCheckCore('admin', 'super')) { die('denied'); }
echo "list " . htmlspecialchars($list);

#!/usr/bin/env php
<?php
// NEGATIVE: CLI script
if (php_sapi_name() !== 'cli') { exit(1); }
echo $_SERVER['argv'][1] ?? '';

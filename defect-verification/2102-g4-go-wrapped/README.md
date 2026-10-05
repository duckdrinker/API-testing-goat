# Gap 4 - Go handlers wrapped in middleware calls
Expected: 6 endpoint rows (GetPost has GET+OPTIONS); handlers unwrapped to controller.GetPost / CreatePost / Home / Plain; auth middleware chain kept for GetPost and CreatePost.
Twin /empty: wrapper without a handler argument must not get a handler.

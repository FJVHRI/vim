

autocmd FileType markdown syntax off
" 针对不同文件类型执行不同操作
" 1. HTML 文件：用 Firefox 打开
autocmd FileType html nnoremap <buffer> <F5> :w<CR> :!firefox-esr --new-tab % &<CR>
"rust 
autocmd FileType rust nnoremap <F5> :w<CR>:!rustc % -o %:r && ./%:r<CR>

" 2. Python 文件：用 Python 执行
autocmd FileType python nnoremap <F5> :w<CR>:!python3 %<CR>

 " 编译并运行 C 程序
autocmd FileType c nnoremap <F5> :w<CR>:!gcc % -o %:r && ./%:r<CR>

" For C files with GTK4
autocmd FileType c nnoremap <F5> :w<CR>:!gcc % -o %:r $(pkg-config --cflags --libs gtk4) && ./%:r<CR>

 " 编译并运行 C++ 程序
autocmd FileType cpp nnoremap <F5> :w<CR>:!g++ % -o %:r && ./%:r<CR>

autocmd FileType cpp nnoremap <F5> :w<CR>:!g++ % -o %:r -lGL -lGLU -lglfw && ./%:r<CR>

" 4. Java 文件：编译并运行
autocmd FileType java nnoremap <buffer> <F5> :W<CR>:!javac % && java %:r<CR>

highlight Comment ctermbg=white ctermfg=blue guibg=white guifg=blue

set fo-=r 
set clipboard=unnamedplus "共享剪切板
au BufReadPost *.txt,*.md if line("'\"") > 1 && line("'\"") <= line("$") | exe "normal! g'\"" | endif
set guioptions=T            " 去除vim的GUI版本中的toolbar
set ruler                           " 打开状态栏标尺

" 定义保存编译并执行代码函数
func! ComplieCode1()
exec "w"
if &filetype == "c"
exec "!gcc -std=gnu99 % -lm && ./a.out"
endif
endfunc

func! ComplieCode2()
exec "w"
if &filetype == "c"
exec "!clang -std=gnu99 % -lm && ./a.out"
endif
endfunc

func! ComplieCode3()
exec "w"
exec "!chmod 755 % && ./%"
endfunction

func NERDTree()
exec "NERDTree"
endfunc
map<C-n> :call NERDTree()<CR>
imap<C-n> <ESC> :call NERDTree()<CR>
vmap<C-n> <ESC> :call NERDTree()<CR>
func Save()
exec "w"
endfunc
map<C-w> :call Save()<CR>
imap<C-w> <ESC> :call Save()<CR>
vmap<C-w> <ESC> :call Save()<CR>

func! OnlyExit()
exec "qa!"
endfunc
map <C-b> :call OnlyExit()<CR>
imap <C-b> <ESC> :call Exit()<CR>
vmap <C-b> <ESC> :call Exit()<CR>


" 映射Ctrl+x键调用gcc编译并执行函数
map <C-x> :call ComplieCode1()<CR>
imap <C-x> <ESC>:call ComplieCode1()<CR>
vmap <C-x> <ESC>:call ComplieCode1()<CR>

" 映射Ctrl+p键调用clang编译并执行函数
map <C-p> :call ComplieCode2()<CR>
imap <C-p> <ESC>:call ComplieCode2()<CR>
vmap <C-p> <ESC>:call ComplieCode2()<CR>

" 映射Ctrl+l键执行shell脚本
map <C-l> :call ComplieCode3()<CR>
imap <C-l> <ESC>:call ComplieCode3()<CR>
vmap <C-l> <ESC>:call ComplieCode3()<CR>

" 映射Ctrl+z键保存并退出
map <C-z> :wq!<CR>
imap <C-z> <ESC>:wq!<CR>
vmap <C-z> <ESC>:wq!<CR>

autocmd BufNewFile *.c exec ":call AddTitleForShell()"
function AddTitleForShell()
"call append(0,"#include <stdio.h>")
"call append(1,"#include <stdlib.h>")
"call append(2,"#include <string.h>")
endfunction

autocmd BufNewFile *.sh exec ":call AddTitleForShell1()"
function AddTitleForShell1()
call append(0,"#!/bin/bash")
endfunction


set signcolumn=yes
highlight SignColumn ctermfg=NONE ctermbg=NONE guifg=NONE guibg=NONE

"set signcolumn=no
set mouse=a
set filetype=python
au BufNewFile,BufRead *.py,*.pyw setf python
set autoindent " same level indent
set smartindent " next level indent
set smartcase " 智能大小写
"set expandtab
"set tabstop=4
set iskeyword=a-z,A-Z
set shiftwidth=4
set softtabstop=4
filetype plugin indent on
let mapleader = ","      " 定义<leader>键
set nocompatible         " 设置不兼容原始vi模式
filetype on              " 设置开启文件类型侦测
filetype plugin on       " 设置加载对应文件类型的插件
set noeb                 " 关闭错误的提示
syntax enable            " 开启语法高亮功能
syntax on                " 自动语法高亮
"set t_Co=256             " 开启256色支持
set cmdheight=2          " 设置命令行的高度
set showcmd              " select模式下显示选中的行数
set ruler                " 总是显示光标位置
set laststatus=2         " 总是显示状态栏,1多窗口显示，0不显示
set number               " 开启行号显示
set cursorline           " 高亮显示当前行
set whichwrap+=<,>,h,l   " 设置光标键跨行
set ttimeoutlen=0        " 设置<ESC>键响应时间
set virtualedit=block,onemore   " 允许光标出现在最后一个字符的后面
" 保留撤销历史
" "
" " Vim 会在编辑时保存操作历史，用来供用户撤消更改
" "
" 默认情况下，操作记录只在本次编辑时有效，一旦编辑结束、文件关闭，操作历史就消失了
" "
" " 打开这个设置，可以在文件关闭后，操作记录保留在一个文件里面，继续存在
" "
" 这意味着，重新打开一个文件，可以撤销上一次编辑时的操作。撤消文件是跟原文件保存在一起的隐藏文件，文件名以.un~开头
set undofile
set  ruler  "在状态栏显示光标的当前位置（位于哪一行哪一列）。"
set showmatch  "光标遇到圆括号、方括号、大括号时，自动高亮对应的另一个圆括号、方括号和大括号。"
" 代码缩进和排版
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
set autoindent           " 设置自动缩进
set cindent              " 设置使用C/C++语言的自动缩进方式
set cinoptions=g0,:0,N-s,(0    " 设置C/C++语言的具体缩进方式
set smartindent          " 智能的选择对其方式
filetype indent on       " 自适应不同语言的智能缩进
set expandtab            " 将制表符扩展为空格
set tabstop=4            " 设置编辑时制表符占用空格数
set shiftwidth=4         " 设置格式化时制表符占用空格数
set softtabstop=4        " 设置4个空格为制表符
set smarttab             " 在行和段开始处使用制表符
set wrap               "折行
set linebreak        "只有遇到指定的符号（比如空格、连词号和其他标点符号），才发生折行。也就是说，不会在单词内部折行。
set wrapmargin=2    "指定折行处与编辑窗口的右边缘之间空出的字符数。
"
set backspace=2          " 使用回车键正常处理indent,eol,start等
set sidescroll=10        " 设置向右滚动字符数
set nofoldenable         " 禁用折叠代码
"
" 代码补全
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
set wildmenu             " vim自身命名行模式智能补全
set completeopt-=preview " 补全时不显示窗口，只显示补全列表
set spell spelllang=en_us   "打开英语单词的拼写检查。"
set autochdir              " 自动切换工作目录。这主要用在一个 Vim 会话之中打开多个文件的情况，默认的工作目录是打开的第一个文件的目录。该配置可以将工作目录自动切换到，正在编辑的文件的目录。"
set autoread  "打开文件监视。如果在编辑过程中文件发生外部改变（比如被别的编辑器编辑了），就会发出提示。"
set visualbell  "出错时，发出视觉提示，通常是屏幕闪烁。"
" 搜索设置
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
set hlsearch            " 高亮显示搜索结果
set incsearch           " 开启实时搜索功能
set ignorecase          " 搜索时大小写不敏感
"
" 缓存设置
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
set nobackup            " 设置不备份
set noswapfile          " 禁止生成临时文件
set autoread            " 文件在vim之外修改过，自动重新读入
set autowrite           " 设置自动保存
set confirm             " 在处理未保存或只读文件的时候，弹出确认
" 编码设置
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
set langmenu=zh_CN.UTF-8
set helplang=cn
set termencoding=utf-8
"set encoding=utf8
set fileencodings=utf8,ucs-bom,gbk,cp936,gb2312,gb18030
"gvim/macvim设置
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
if has("gui_running")
let system = system('uname -s')
if system == "Darwin\n"
        set guifont=Droid\ Sans\ Mono\ Nerd\ Font\ Complete:h18 " 设置字体
    else
        set guifont=DroidSansMono\ Nerd\ Font\ Regular\ 18      " 设置字体
    endif
    set guioptions-=m           " 隐藏菜单栏
    set guioptions-=T           " 隐藏工具栏
    set guioptions-=L           " 隐藏左侧滚动条
    set guioptions-=r           " 隐藏右侧滚动条
    set guioptions-=b           " 隐藏底部滚动条
    set showtabline=0           " 隐藏Tab栏
    set guicursor=n-v-c:ver5    " 设置光标为竖线
endif
" 卸载默认插件UnPlug
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
function! s:deregister(repo)
  let repo = substitute(a:repo, '[\/]\+$', '', '')
  let name = fnamemodify(repo, ':t:s?\.git$??')
  call remove(g:plugs, name)
endfunction
command! -nargs=1 -bar UnPlug call s:deregister(<args>)

" 插件列表
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
call plug#begin('~/.vim/plugged')
Plug 'tikhomirov/vim-glsl'

"Plug 'vim-latex/vim-latex-suite'
"Plug 'dense-analysis/ale'
"Plug 'jupyter-vim/jupyter-vim'
"Plug 'vim-syntastic/syntastic'
Plug 'Yggdroot/indentLine'
"Plug 'w0rp/ale'

let g:ale_sign_column_always = 1
let g:ale_set_highlights = 0
let g:ale_sign_error = 'x'
let g:ale_sign_warning = '!'
let g:ale_lint_on_text_changed = 'never'
let g:ale_lint_on_enter = 0
let g:ale_linters = {
\   'c++': ['clangd'],
\   'c': ['clangd'],
\   'python': ['pylint'],
\   'rust': ['cargo', 'clippy']
\}
let g:ale_python_black_executable = 'black'
let g:ale_fixers = {
            \   'python': ['black'],
            \   'rust': ['cargo', 'rustfmt']
            \}
let g:ale_rust_cargo_use_clippy = 1 " 使用 clippy 进行更严格的检查
let g:ale_fix_on_save = 1  " 保存文件时自动格式化
"

"Plug 'SirVer/ultisnips'
Plug 'honza/vim-snippets'
let g:UltiSnipsExpandTrigger="<tab>"
let g:UltiSnipsJumpForwardTrigger="<c-h>"
let g:UltiSnipsJumpBackwardTrigger="<c-l>"
"Plug 'Vim-cn/elimage'
"Plug 'ianva/vim-youdao-translater'
"Plug 'bujnlc8/vim-translator'
"Plug 'asins/vim-dict'
"Plug 'chxuan/cpp-mode'
Plug 'chxuan/vim-edit'
"Plug 'chxuan/change-colorscheme'
"Plug 'chxuan/prepare-code'
"Plug 'chxuan/vim-buffer'
Plug 'chxuan/vimplus-startify'
Plug 'preservim/tagbar'
Plug 'neoclide/coc.nvim', {'branch': 'release'}
Plug 'neoclide/coc-snippets', {'branch': 'master'}

"Plug 'mattn/emmet-vim'
"Plug 'ryanoasis/vim-devicons'
"Plug 'junegunn/vim-slash'
"Plug 'junegunn/gv.vim'
Plug 'kana/vim-textobj-user'
Plug 'kana/vim-textobj-indent'
Plug 'kana/vim-textobj-syntax'
Plug 'kana/vim-textobj-function'
Plug 'sgur/vim-textobj-parameter'
"Plug 'Shougo/echodoc.vim'
Plug 'terryma/vim-smooth-scroll'
Plug 'rhysd/clever-f.vim'
Plug 'vim-scripts/indentpython.vim'
"Plug 'Yggdroot/LeaderF'
"Plug 'mileszs/ack.vim'
"Plug 'vim-airline/vim-airline'
"Plug 'vim-airline/vim-airline-themes'
"Plug 'easymotion/vim-easymotion'
"Plug 'haya14busa/incsearch.vim'
"Plug 'jiangmiao/auto-pairs'
Plug 'preservim/nerdtree'
Plug 'tiagofumo/vim-nerdtree-syntax-highlight'

Plug 'Xuyuanp/nerdtree-git-plugin'
"Plug 'godlygeek/tabular'
"Plug 'tpope/vim-fugitive'
Plug 'tpope/vim-surround'
Plug 'tpope/vim-commentary'
Plug 'tpope/vim-repeat'
Plug 'tpope/vim-endwise'
"Plug 'octol/vim-cpp-enhanced-highlight'
"Plug 'drichardson/Vim-Unreal'
"Plug 'alvan/vim-closetag'
Plug 'bfrg/vim-cuda-syntax'
"Plug 'plasticboy/vim-markdown'
"Plug 'mbbill/undotree'

"Plug '3rd/image.nvim'

" 插件安装
"Plug 'stevearc/dressing.nvim'
"Plug 'nvim-lua/plenary.nvim'
"Plug 'MunifTanjim/nui.nvim'
Plug 'luochen1990/rainbow'
" 可选依赖
"Plug 'hrsh7th/nvim-cmp'
"Plug 'nvim-tree/nvim-web-devicons' " 或 Plug 'echasnovski/mini.icons'
"Plug 'HakonHarnes/img-clip.nvim'
"Plug 'zbirenbaum/copilot.lua'

" 构建插件
"Plug 'yetone/avante.nvim', {'branch': 'main', 'do': 'make'}
"Plug 'Exafunction/codeium.vim',{'branch':'main'}
"let g:codeium_enabled = v:true
"Plug 'ggml-org/llama.vim'
"let g:llama_config.show_info = 0
"let g:closetag_html_style = 1
Plug 'voldikss/vim-floaterm'
"加载自定义插件
if filereadable(expand($HOME . '/.vimrc.custom.plugins'))
    source $HOME/.vimrc.custom.plugins
endif
call plug#end()
let g:rainbow_active = 1
let g:indentLine_enabled = 1
let g:indentLine_char_list = ['|', '¦', '┆', '┊']
let g:indentLine_color_term = 239
let g:indentLine_color_gui = '#A4E57E'

" Enable coc.nvim
filetype plugin indent on
syntax on
set hidden

" coc.nvim 每次跳到下一个占位符时，自动全选旧内容
let g:coc_snippet_next = '<Tab>'
let g:coc_snippet_prev = '<S-Tab>'
" 关键：让 coc 把占位符内容选起来，这样直接输入就会覆盖
autocmd User CocJumpPlaceholder call CocActionAsync('showSignatureHelp')
" load vim default plugin
runtime macros/matchit.vim
" 启用 Markdown 文件类型检测
autocmd FileType markdown setlocal wrap       " 自动换行
autocmd FileType markdown setlocal spell      " 启用拼写检查
autocmd FileType markdown setlocal spelllang=en_us " 设置拼写检查语言
" "
" 显示 undotree 视图时的宽度
"let g:undotree_WindowLayout = 2        " 窗口布局
"let g:undotree_SetFocusWhenOpen = 1    " 打开时将焦点设置到 undotree
"let g:undotree_HelpLine = 1             " 启用帮助行
" "

" 启用自动生成标签
let g:gutentags_ctags_bin = 'ctags'  " 使用 ctags
let g:gutentags_ctags_args = ['--fields=+l', '--extra=+q']  "
" 其他参数可根据需要调整
let g:gutentags_cache_dir = expand('~/.vim/gutentags_cache')  " 标签缓存目录
"" 项目特定配置（可选）
let g:gutentags_project_root = ['.git', '.hg', '.svn', 'Makefile', 'CMakeLists.txt']
" 设置浮动终端的快捷键
let g:floaterm_keymap_new = '<F6>'
let g:floaterm_keymap_prev = '<F7>'
let g:floaterm_keymap_next = '<F8>'
let g:floaterm_keymap_toggle = '<F9>' "隐藏当前终端：按 <F12>"
nnoremap <silent> <F10> :FloatermKill<CR>
" 设置浮动终端的位置
let g:floaterm_position = 'center'
" 设置自动关闭行为
let g:floaterm_autoclose = 2        " 0: 保留, 1: 立即关闭, 2: 完成后关闭"
" 设置浮动终端外观
let g:floaterm_title = 'Floaterm'   " 设置浮动终端的标题
let g:floaterm_border = 'single'     " 边框样式，可选择 'none', 'single',
" 'double', 'rounded' 等

" 终端命令配置（可选）
let g:floaterm_autocd = 1
let g:floaterm_default_mod = 'vertical'  " 默认打开终端的方式（'horizontal'或 'vertical'）"

" 编辑vimrc相关配置文件
nnoremap <leader>e :edit $MYVIMRC<cr>
nnoremap <leader>vc :edit ~/.vimrc.custom.config<cr>
nnoremap <leader>vp :edit ~/.vimrc.custom.plugins<cr>

" 查看vimplus的help文件
nnoremap <leader>h :view +let\ &l:modifiable=0 ~/.vimplus/help.md<cr>

" 打开当前光标所在单词的vim帮助文档
nnoremap <leader>H :execute ":help " . expand("<cword>")<cr>

" 重新加载vimrc文件
nnoremap <leader>s :source $MYVIMRC<cr>

" 安装、更新、删除插件
nnoremap <leader><leader>i :PlugInstall<cr>
nnoremap <leader><leader>u :PlugUpdate<cr>
nnoremap <leader><leader>c :PlugClean<cr>

" 分屏窗口移动
nnoremap <c-j> <c-w>j
nnoremap <c-k> <c-w>k
nnoremap <c-h> <c-w>h
nnoremap <c-l> <c-w>l



" 复制当前选中到系统剪切板
vmap <leader><leader>y "+y

" 将系统剪切板内容粘贴到vim
nnoremap <leader><leader>p "+p

" 打开文件自动定位到最后编辑的位置
autocmd BufReadPost * if line("'\"") > 1 && line("'\"") <= line("$") | execute "normal! g'\"" | endif

let g:airline_powerline_fonts = 1
let g:airline#extensions#tabline#enabled = 1

if !exists('g:airline_symbols')
    let g:airline_symbols = {}
endif
let g:airline_left_sep = ''
let g:airline_left_alt_sep = ''
let g:airline_right_sep = ''
let g:airline_right_alt_sep = ''

let g:coc_global_extensions = [
        \ 'coc-marketplace',
        \ '@yaegassy/coc-volar',
        \ 'coc-tsserver',
        \ 'coc-json',
        \ 'coc-html',
        \ 'coc-css',
        \ 'coc-clangd',
        \ 'coc-go',
        \ 'coc-sumneko-lua',
        \ 'coc-vimlsp',
        \ 'coc-sh',
        \ 'coc-db',
        \ 'coc-pyright',
        \ 'coc-toml',
        \ '@nomicfoundation/coc-solidity',
        \ 'coc-prettier',
        \ 'coc-snippets',
        \ 'coc-pairs',
        \ 'coc-word',
        \ 'coc-translator',
        \ 'coc-git',
        \ '@yaegassy/coc-tailwindcss3',
        \'coc-diagnostic',
        \'coc-terminal',
        \'coc-xml',
        \'coc-java'
        \]

"let g:coc_explorer_global_presets = {
\   '.vim': {
\     'root-uri': '~/.vim',
\   },
\   'cocConfig': {
\      'root-uri': '~/.config/coc',
\   },
\   'tab': {
\     'position': 'tab',
\     'quit-on-open': v:true,
\   },
\   'tab:$': {
\     'position': 'tab:$',
\     'quit-on-open': v:true,
\   },
\   'floating': {
\     'position': 'floating',
\     'open-action-strategy': 'sourceWindow',
\   },
\   'floatingTop': {
\     'position': 'floating',
\     'floating-position': 'center-top',
\     'open-action-strategy': 'sourceWindow',
\   },
\   'floatingLeftside': {
\     'position': 'floating',
\     'floating-position': 'left-center',
\     'floating-width': 50,
\     'open-action-strategy': 'sourceWindow',
\   },
\   'floatingRightside': {
\     'position': 'floating',
\     'floating-position': 'right-center',
\     'floating-width': 50,
\     'open-action-strategy': 'sourceWindow',
\   },
\   'simplify': {
\     'file-child-template': '[selection | clip | 1] [indent][icon | 1] [filename omitCenter 1]'
\   },
\   'buffer': {
"\     'sources': [{'name': 'buffer', 'expand': v:true}]
"\   },
"\   'project-root': {
"\     'root-uri-strategy': 'project',
"\   },
"\ }

" Use preset argument to open it
"nmap <space>ed <Cmd>CocCommand explorer --preset .vim<CR>
"nmap <space>ef <Cmd>CocCommand explorer --preset floating<CR>
"nmap <space>ec <Cmd>CocCommand explorer --preset cocConfig<CR>
"nmap <space>eb <Cmd>CocCommand explorer --preset buffer<CR>
"nmap <space>ep <Cmd>CocCommand explorer --preset project-root<CR>
" List all presets
"nmap <space>el <Cmd>CocList explPresets<CR>
"nnoremap - <C-w>p
" cpp-mode
nnoremap <leader>y :CopyCode<cr>
nnoremap <leader>p :PasteCode<cr>
nnoremap <leader>U :GoToFunImpl<cr>
nnoremap <silent> <leader>a :Switch<cr>
nnoremap <leader><leader>fp :FormatFunParam<cr>
nnoremap <leader><leader>if :FormatIf<cr>
nnoremap <leader><leader>t dd :GenTryCatch<cr>
xnoremap <leader><leader>t d :GenTryCatch<cr>
" prepare-code
let g:prepare_code_plugin_path = expand($HOME . "/.vim/plugged/prepare-code")

" vim-buffer
nnoremap <silent> <c-p> :PreviousBuffer<cr>
nnoremap <silent> <c-n> :NextBuffer<cr>
nnoremap <silent> <leader>d :CloseBuffer<cr>
nnoremap <silent> <leader>D :BufOnly<cr>

" vim-edit
nnoremap Y :CopyText<cr>
nnoremap D :DeleteText<cr>
nnoremap C :ChangeText<cr>
nnoremap <leader>r :ReplaceTo<space>

" nerdtree
nnoremap <silent> <leader>n :NERDTreeToggle<cr>
let NERDTreeShowHidden=1
let g:NERDTreeFileExtensionHighlightFullName = 1
let g:NERDTreeExactMatchHighlightFullName = 1
let g:NERDTreePatternMatchHighlightFullName = 1
let g:NERDTreeHighlightFolders = 1
let g:NERDTreeHighlightFoldersFullName = 1
let g:NERDTreeDirArrowExpandable='▷'
let g:NERDTreeDirArrowCollapsible='▼'
"nnoremap <silent> - :NERDTreeToggle<cr>
" 切换焦点到文件树
nnoremap <silent> - :NERDTreeFocus<cr>
" 切换焦点到文件
nnoremap <silent> - :wincmd p<cr>
" tagbar
let g:tagbar_width = 30
nnoremap <silent> <leader>t :TagbarToggle<cr>
" 让 Tagbar 在右侧
let g:tagbar_position = 'right'
" 自动隐藏空标签
let g:tagbar_compact = 1
" 通过 - 键切换 Tagbar
nnoremap <silent> - :TagbarToggle<cr>

" incsearch.vim
"map /  <Plug>(incsearch-forward)
"map ?  <Plug>(incsearch-backward)
"map g/ <Plug>(incsearch-stay)

" vim-easymotion
let g:EasyMotion_smartcase = 1
map <leader>w <Plug>(easymotion-bd-w)
nmap <leader>w <Plug>(easymotion-overwin-w)
" nerdtree-git-plugin
let g:NERDTreeGitStatusIndicatorMapCustom = {
            \ "Modified"  : "✹",
            \ "Staged"    : "✚",
            \ "Untracked" : "✭",
            \ "Renamed"   : "➜",
            \ "Unmerged"  : "═",
            \ "Deleted"   : "✖",
            \ "Dirty"     : "✗",
            \ "Clean"     : "✔︎",
            \ 'Ignored'   : '☒',
            \ "Unknown"   : "?"
            \ }

" LeaderF
nnoremap <leader>f :LeaderfFile .<cr>
let g:Lf_WildIgnore = {
            \ 'dir': ['.svn','.git','.hg','.vscode','.wine','.deepinwine','.oh-my-zsh'],
            \ 'file': ['*.sw?','~$*','*.bak','*.exe','*.o','*.so','*.py[co]']
            \}
let g:Lf_UseCache = 0

" ack
nnoremap <leader>F :Ack!<space>

" echodoc.vim
let g:echodoc_enable_at_startup = 1

" tabular
nnoremap <leader>l :Tab /\|<cr>
nnoremap <leader>= :Tab /=<cr>

" vim-smooth-scroll
noremap <silent> <c-u> :call smooth_scroll#up(&scroll, 0, 2)<CR>
noremap <silent> <c-d> :call smooth_scroll#down(&scroll, 0, 2)<CR>
noremap <silent> <c-b> :call smooth_scroll#up(&scroll*2, 0, 4)<CR>
noremap <silent> <c-f> :call smooth_scroll#down(&scroll*2, 0, 4)<CR>

" gv
nnoremap <leader>g :GV<cr>
nnoremap <leader>G :GV!<cr>
nnoremap <leader>gg :GV?<cr>

" 加载自定义配置
if filereadable(expand($HOME . '/.vimrc.custom.config'))
    source $HOME/.vimrc.custom.config
endif
"highlight Normal guibg=NONE ctermbg=NONE " 使普通背景透明
"highlight NonText guibg=NONE ctermbg=NONE " 使非文本背景透明
"highlight LineNr guibg=NONE ctermbg=NONE " 行号透明
"highlight CursorLine ctermbg=none guibg=none

command! -nargs=? Fold call CocAction('fold', <f-args>)
hi! link CocPum Pmenu
hi! link CocMenuSel PmenuSel

nnoremap <silent> <F2> <Plug>(coc-rename)
nnoremap <silent> gd <Plug>(coc-definition)
nnoremap <silent> gy <Plug>(coc-type-definition)
nnoremap <silent> gi <Plug>(coc-implementation)
nnoremap <silent> gr <Plug>(coc-references)

xnoremap <silent> if <Plug>(coc-funcobj-i)
onoremap <silent> if <Plug>(coc-funcobj-i)
xnoremap <silent> af <Plug>(coc-funcobj-a)
onoremap <silent> af <Plug>(coc-funcobj-a)
xnoremap <silent> ic <Plug>(coc-classobj-i)
onoremap <silent> ic <Plug>(coc-classobj-i)
xnoremap <silent> ac <Plug>(coc-classobj-a)
onoremap <silent> ac <Plug>(coc-classobj-a)
inoremap <silent><expr> <CR> coc#pum#visible() ? coc#pum#confirm() : "\<C-g>u\<CR>\<c-r>=coc#on_enter()\<CR>"

"inoremap <silent> <C-f> (coc#pum#visible() ? '<C-y>' : '<C-f>')
"inoremap <silent> <TAB>\ (coc#pum#visible() ? coc#pum#next(1) : (col('.') == 1 || getline('.')[col('.') - 2] =~# '\s' ? "\<TAB>" : coc#refresh()))
"inoremap <silent> <TAB> (coc#pum#visible() ? coc#pum#next(1) : (line('.') > 1 && (col('.') == 1 || getline('.')[col('.') - 2] =~# '\s') ? "\TAB<" : coc#refresh"()))>"))
inoremap <silent> <S-TAB> (coc#pum#visible() ? coc#pum#prev(1) : "<S-TAB>")

inoremap <silent> <C-y> (coc#pum#visible() ? coc#pum#confirm() : '<C-y>')
"nnoremap <silent> <F3> :silent CocRestart<CR>
nnoremap <silent><F4> get(g:, 'coc_enabled', 0) == 1 ? ':CocDisable<CR>' : ':CocEnable<CR>'
nnoremap <silent> <F12> :CocCommand snippets.editSnippets<CR>
nnoremap <silent> <C-e> :CocList --auto-preview diagnostics<CR>
nnoremap <silent> mm <Plug>(coc-translator-p)
vnoremap <silent> mm <Plug>(coc-translator-pv)
vnoremap mr <Plug>(coc-translator-rv)
nnoremap <silent> ( <Plug>(coc-git-prevchunk)
nnoremap <silent> ) <Plug>(coc-git-nextchunk)
nnoremap <silent> C get(b:, 'coc_git_blame', '') ==# 'Not committed yet' ? "<Plug>(coc-git-chunkinfo)" : "<Plug>(coc-git-commit)"
"nnoremap <silent> \g :call coc#config('git.addGBlameToVirtualText',  !get(g:coc_user_config, 'git.addGBlameToVirtualText', 0)) | call nvim_buf_clear_namespace(bufnr(), -1, line('.') - 1, line('.'))<CR>
nnoremap <silent> \g :call coc#config('git.addGBlameToVirtualText', !get(g:coc_user_config, 'git.addGBlameToVirtualText', 0))CR<>
xnoremap <silent> (CocHasProvider("formatRange") ? "<Plug>(coc-format-selected)" : "=")
nnoremap <silent> = (CocHasProvider("formatRange") ? "<Plug>(coc-format-selected)" : "=")

"function! s:setup()
" do nothing
"endfunction
"return s:setup()
highlight CocFloating ctermbg=NONE guibg=white
highlight CocMenuSel ctermbg=LightBlue  guibg=LightGreen
highlight CocMenu ctermbg=NONE 
" 设置状态栏格式
set statusline=%%F%=%y%<m%r%h%w%{&ff}\[%{&fenc}]0x%02B@%040h#%n\(%3l/%3L,%3c\|%3v\)%3p%%
set statusline=%1*\%%.50F\            "显示文件名和文件路径 
set statusline+=%=%2*\%y%m%r%h%w\ %*        "显示文件类型及文件状态
set statusline+=%3*\%{&ff}\[%{&fenc}]\ %*   "显示文件编码类型
set statusline+=%4*\ row:%l/%L,col:%c\ %*   "显示光标所在行和列
set statusline+=%5*\%3p%%\%*            "显示光标前文本所占总文本的比例

function! AddFileInformation_html()
    let infor = "<!DOCTYPE html>\n"
    \."<html lang=\"en\">\n"
    \."<head>\n"
    \."    <meta charset=\"UTF-8\">\n"
    \."    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
    \."    <title> </title>\n"
    \."    <link rel=\"stylesheet\" href=\"\">\n"
    \."</head>\n"
\. "\n"
    \."<body>\n"
    \. "\n"
    \."</body>\n"
\. "\n"
    \."</html>\n"
silent put! =infor
endfunction
hi User1 cterm=none ctermfg=25 ctermbg=NONE
hi User2 cterm=none ctermfg=208 ctermbg=NONE
hi User3 cterm=none ctermfg=100  ctermbg=NONE
hi User4 cterm=none ctermfg=100 ctermbg=NONE
hi User5 cterm=none ctermfg=green ctermbg=NONE


autocmd BufNewFile *.html call AddFileInformation_html()

" 在源文件和头文件之间切换
nnoremap <silent> <leader>a :execute "edit " . substitute(expand("%:p"), "\\(.\\{-}\\)\\(\\.[ch]\\+\\)$", "\\1." . ({'c': 'h', 'h': 'c'}[submatch(2)[1]]), "")<CR>

function! Mydict()
    " 获取光标下的单词
    let word = expand("<cword>")
    " 调用 sdcv 查询单词，并保存结果
    let expl = system('sdcv -n ' . word)

    " 如果文件是 diCt-tmp，关闭它
    windo if expand("%") == "diCt-tmp"
        quit
    endif

    " 打开临时文件并显示解释
    vsplit diCt-tmp
    setlocal buftype=nofile bufhidden=hide noswapfile

    " 在文件顶部插入查询的单词解释
    call setline(1, expl)
endfunction

" 映射 F 键调用 Mydict 函数
nmap <silent> F :call Mydict()<CR>

" 自动插入 C++ 文件模板
augroup CPPFileTemplate
  autocmd!
  autocmd BufNewFile *.cpp call InsertCPPTemplate()
augroup END

function! InsertCPPTemplate()
  let className = substitute(expand('%:t'), '\.cpp$', '', '')

  " 一次性写入模板，头文件与 main 之间空一行
  0put =[
        \ '#include <iostream>',
        \ '',
        \ 'int main(int argc, char *argv[]) {',
        \ '     ',
        \ '    std::cout << << std::endl;',
        \ '    return 0;',
        \ '}'
        \ ]

call cursor(4, 5)
endfunction

" 自动插入 C 文件模板
augroup CFileTemplate
  autocmd!
  autocmd BufNewFile *.c call InsertCTemplate()
augroup END

function! InsertCTemplate()
  let className = substitute(expand('%:t'), '\.c$', '', '')

  " 一次性写入模板，头文件与 main 之间空一行
  0put =[
        \ '#include <stdio.h>',
        \ '',
        \ 'int main(int argc, char *argv[]) {',
        \ '     ',
        \ '    printf(\"\n\");',
        \ '    return 0;',
        \ '}'
        \ ]

call cursor(4, 5)
endfunction 

augroup JavaFileTemplate
  autocmd!
  autocmd BufNewFile *.java call InsertJavaTemplate()
augroup END

function! InsertJavaTemplate()
  let className = substitute(expand('%:t'), '\.java$', '', '')
  let lines = [
        \ 'public class ' . className . ' {',
        \ '    public static void main(String[] args) {',
        \ '         ',
        \ '        System.out.println("");',
        \ '    }',
        \ '}'
        \ ]
  call append(0, lines)   " 在第 0 行后插入
  call cursor(3, 9)     
  echomsg 'Cursor position: ' . line('.') . ',' . col('.')
  redraw 
endfunction
set nospell


augroup custom_cursor_position
  autocmd!
  " 在插入模式中换行后自动调整光标
  autocmd InsertEnter * call AdjustCursor()
augroup END

function! AdjustCursor()
  " 获取光标当前位置
  let l:current_line = line('.')
  " 获取当前行的第一个非空白字符的位置
  let l:first_non_blank = match(getline(l:current_line), '\S')
  " 将光标移动到上一行的第一个非空白字符的下一列
  if l:first_non_blank != -1
    normal! k0
    execute "normal! " . (l:first_non_blank + 1) . "|"
  endif
endfunction

"取消换行自动注释
au FileType c,cpp,rust,java,js,go,html setlocal comments-=:// comments+=f://
"取消自动缩进
"set paste

" 自动命令，当文件类型为 C 或 C++ 时，检查 #include<> 是否为空
autocmd FileType c,cpp autocmd CursorMovedI * call AutoJumpInclude()

" 检查 #include<> 是否为空，以及光标是否位于尖括号内部
function! AutoJumpInclude() abort
  let col = col('.') - 1
  let line = getline('.')

  " 检查是否为 #include< > 且光标在尖括号内
  if line[col - 1] =~# '^\s*#\s*include\s*<.*>$' && line[col] == '>'
    " 检查尖括号内是否有内容
    let include_content = matchstr(line, '\v#\s*include\s*<([^>]*)>')

    " 如果尖括号内有内容，跳到尖括号的右侧并换行
    if len(include_content) > 0
      execute "normal! \<Right>\<CR>"
    endif
  endif
endfunction

inoremap <expr> <Tab> SmartTab()

function! SmartTab() abort
  " ---------- 括号场景 ----------
  let [lnum, cnum] = searchpairpos('(', '', ')', 'bnW')
  if lnum > 0
    call cursor(lnum, cnum)
    let [rl, rc] = searchpos(')', 'Wc')
    if !empty(rl)
      call cursor(rl, rc)
      let tail = matchstr(getline('.')[rc :], '\m^\s*\zs.')
      if tail == '{'
        " 跳到 { 下一行，行首直接插入
        return "\<C-o>:call search('{', 'Wc')\<CR>\<C-o>o"
      elseif tail == ';'
        " 把 ); 放在同一行，光标跳到 ; 后回车
        return "\<C-o>:call search(';', 'Wc')\<CR>\<C-o>a\<CR>"
      endif
    endif
  endif

  " ---------- 运算符场景 ----------
  let line = getline('.')
  let col  = col('.') - 1
  let opPat = '\m[=+\-*/]\+'
  let start = match(line, opPat, col)
  if start != -1
    let opLen  = len(matchstr(line, opPat, start))
    let target = start + opLen + 1
    if getline('.')[start + opLen] != ' '
      return repeat("\<Del>", target - col - 1) . ' '
    endif
    return repeat("\<Right>", target - col)
  endif

  return "\<Tab>"
endfunction
nnoremap <leader>s :Startify<CR>
" 回到刚才编辑的文件
nnoremap <leader>S :b#<CR>

#!/bin/bash
PATH="/sbin:/usr/sbin:/bin:/usr/bin:/usr/local/sbin:/usr/local/bin"
export PATH

# 帮助函数放在最开始，方便调用
function usage() {
    echo "Usage: $0 -p <platform> -v <version> -m <publish mode> -l <client branch>"
    echo
    echo "Example:"
    echo "$0 -p android -v 01.50.00 -m intra -l master"
    echo
    exit 1
}

function echoerr() {
    echo "$@" 1>&2;
}

############### 需要的全局变量 ###############
VERSION=`date +%Y%m%d`
PLATFORM="android"
MODE="intra"
CLIENT_BRANCH="master"
SCRIPT_DIR=$(cd "$(dirname "$0")"; pwd)
PROJECT_NAME="AULive"

############### 参数分析 ###############
# option_string以冒号开头表示屏蔽脚本的系统提示错误，自己处理错误提示。
# 后面接合法的单字母选项，选项后若有冒号，则表示该选项必须接具体的参数
while getopts ":p:v:m:l:" OPTION; do
    case "${OPTION}" in
        p)
            PLATFORM=${OPTARG}
            ;;
        v)
            VERSION=${OPTARG}
            ;;
        m)
            MODE=${OPTARG}
            ;;
        l)
            CLIENT_BRANCH=${OPTARG}
            ;;
        *)
            usage
            ;;
    esac
done
# $OPTIND为特殊变量，表示第几个选项，初始值为1
shift $((OPTIND-1))

if [ -z "$VERSION" ]; then
    echoerr "You must specify VERSION with -v option"
    exit 1
fi
if [ -z "$CLIENT_BRANCH" ]; then
    echoerr "You must specify git branch of ACTMobileClient with -l option"
    exit 1
fi

############### 干活 ###############
cd $SCRIPT_DIR/../../
git reset --hard
# 先拉一次是为了新分支能正常checkout
git pull
git clean -fdx
git checkout $CLIENT_BRANCH

# copy resource


if [ "$PLATFORM" = "android" ]; then
    cd $SCRIPT_DIR/../../$PROJECT_NAME
    gradle assembleyingyongbaoRelease

    # 如果返回1，那么说明编译出错了
    if [ $? -ne 0 ]; then
        echoerr “Android compiled with error, please check it.”
        exit 1
    fi

    cp $SCRIPT_DIR/../../$PROJECT_NAME/build/outputs/apk/$PROJECT_NAME-yingyongbao-release.apk $SCRIPT_DIR/../../$PROJECT_NAME/t_$VERSION.apk
    lftp -u taohua,up.taohua.123u art.123u.com <<EOF
    mput t_$VERSION.apk
    bye
EOF
fi

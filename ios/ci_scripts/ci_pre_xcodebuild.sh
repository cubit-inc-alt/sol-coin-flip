#!/bin/zsh

brew install openjdk@21

OPENJDK_PATH="$(brew --prefix openjdk@21)"

export JAVA_HOME="$OPENJDK_PATH/libexec/openjdk.jdk/Contents/Home"
export PATH="$OPENJDK_PATH/bin:$PATH"

echo "
export JAVA_HOME=$JAVA_HOME
export PATH=$OPENJDK_PATH/bin:\$PATH
" >> ~/.zshrc

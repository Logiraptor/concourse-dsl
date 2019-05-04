# Bulk of this source taken (with modifications) from https://github.com/holgerbrandl/kscript/blob/8bf4a6d91c4f584cb0403653fb26fa1937b3324c/misc/Dockerfile

# The MIT License (MIT)
#
# Copyright (c) 2017 Holger Brandl
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.

# Base Image for kscript testing
FROM ubuntu

## update apt cache
RUN  apt-get update

RUN apt-get install -y curl unzip zip wget git

## install java
RUN  apt-get install -y openjdk-8-jdk

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/

# install sdkman (see http://sdkman.io/install.html)
RUN bash -c "curl -s 'https://get.sdkman.io' | bash"

# install assert.h
RUN bash -c "cd /bin && wget https://raw.githubusercontent.com/lehmannro/assert.sh/master/assert.sh && chmod u+x assert.sh"

RUN /bin/bash -c 'source ~/.sdkman/bin/sdkman-init.sh && sdkman_auto_answer=true && sdk install kotlin'
RUN /bin/bash -c 'source ~/.sdkman/bin/sdkman-init.sh && sdkman_auto_answer=true && sdk install maven'
RUN /bin/bash -c 'source ~/.sdkman/bin/sdkman-init.sh && sdkman_auto_answer=true && sdk install gradle 4.10'
RUN /bin/bash -c 'source ~/.sdkman/bin/sdkman-init.sh && sdkman_auto_answer=true && sdk install kscript'

RUN wget "https://github.com/concourse/concourse/releases/download/v5.1.0/fly-5.1.0-linux-amd64.tgz"

RUN tar -xvf "fly-5.1.0-linux-amd64.tgz"
RUN mv fly /usr/local/bin/fly
RUN rm "fly-5.1.0-linux-amd64.tgz"

ADD . /workdir

WORKDIR /workdir


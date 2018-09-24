FROM aegeus/aegeus-ipfsj

# Install the binaries
COPY aegeus-dist-1.0.0.Alpha4-SNAPSHOT aegeus-webui

# Make the entrypoint executable
RUN ln -s /aegeus-webui/bin/run-aegeus-webui.sh /usr/local/bin/aegeus-webui

ENTRYPOINT ["aegeus-webui"]

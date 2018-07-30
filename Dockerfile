FROM node:latest

RUN mkdir parse

ADD . /parse
WORKDIR /parse
RUN npm install

ENV APP_ID fbu-2018-jumble
ENV MASTER_KEY fbu-2018-key
ENV DATABASE_URI 'mongodb://heroku_j8mkx7b1:2fgh2tjh27i9a88qimbdihsnvh@ds135441.mlab.com:35441/heroku_j8mkx7b1'

# Optional (default : 'parse/cloud/main.js')
# ENV CLOUD_CODE_MAIN cloudCodePath

# Optional (default : '/parse')
# ENV PARSE_MOUNT mountPath

EXPOSE 1337

# Uncomment if you want to access cloud code outside of your container
# A main.js file must be present, if not Parse will not start

# VOLUME /parse/cloud               

CMD [ "npm", "start" ]

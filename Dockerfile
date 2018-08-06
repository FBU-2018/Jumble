FROM node:latest

RUN mkdir parse

ADD . /parse
WORKDIR /parse
RUN npm install

ENV APP_ID fbu-2018
ENV MASTER_KEY fbu-2018-key
ENV DATABASE_URI mongodb://heroku_86l8nj5d:nthvbkpajohdnjqtbd2vjspvbv@ds111562.mlab.com:11562/heroku_86l8nj5d

ENV FCM_API_KEY AAAAto1Azj4:APA91bEEDuiH6lHJyX4fsOn9RZfIW5f5UvizIl0NZeJV-m2y7fBdgUJiOPQC-FPsBwryzYvuo6RmpOx65X3q_KPUwPRR_N_E5HEa27enEms38Q08_AqtmBVYS366UGn5fg4iVrmQD0Zc9CxxwI5b-Z3zRut0mFn3lQ
ENV SERVER_URL https://fbu-2018.herokuapp.com/parse

# Optional (default : 'parse/cloud/main.js')
ENV CLOUD_CODE_MAIN cloud/main.js

# Optional (default : '/parse')
# ENV PARSE_MOUNT mountPath

EXPOSE 1337

# Uncomment if you want to access cloud code outside of your container
# A main.js file must be present, if not Parse will not start

# VOLUME /parse/cloud               

CMD [ "npm", "start" ]

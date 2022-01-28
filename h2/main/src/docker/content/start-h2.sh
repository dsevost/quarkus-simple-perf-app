#!/bin/bash

BASE_DIR=${BASE_DIR:-/home/jboss/h2-data}
SERVER_ARGS=${SERVER_ARGS:- -tcpAllowOthers}

case $DEBUG in
    [Tt][Rr][Uu][Ee])
        cat << EOF
            BASE_DIR=$BASE_DIR
            DB_USER=$DB_USER
            DB_PASSWORD=$DB_PASSWORD
            DB_NAME=$DB_NAME
EOF
        ;;
esac

[ -d $BASE_FIR ] || mkdir -p $BASE_DIR

[ "$DB_NAME" = "" ] || \
    java -cp /home/jboss/h2.jar org.h2.tools.Shell -url jdbc:h2:file:$BASE_DIR/$DB_NAME -sql exit

[ "$DB_USER" != "" && "$DB_PASSWORD" != "" ] && \
    java -cp /home/jboss/h2.jar org.h2.tools.Shell -url jdbc:h2:file:$BASE_DIR/$DB_NAME -sql "CREATE USER IF NOT EXISTS $DB_USER PASSWORD '$DB_PASSWORD' ADMIN;"

#case $EXECUTE_SCRIPT_ON_START in
#    [Tt][Rr][Uu][Ee]|[Yy][Ee][Ss])
#        java -cp /home/jboss/h2.jar org.h2.tools.RunScript -url jdbc:h2:file:$BASE_DIR/$DB_NAME -script $EXECURE_SCRIPT_NAME
#        ;;
#esac

exec java -cp /home/jboss/h2.jar org.h2.tools.Server -baseDir $BASE_DIR $SERVER_ARGS

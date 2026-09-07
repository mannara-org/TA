
SOURCES := $(shell find src/main/java -name '*.java')
BUILD_DIR := bin
MARKER := $(BUILD_DIR)/.compiled

MODELS_LIST := $(shell find ./src/main/java/model/ -type f -exec basename -s .java {} \;)
DB_PATH := ./data/WhatAreTheyWorth.db

CLASSPATH := lib/*
MODULES := javafx.controls

TEST := ./src/test/java/Main.java
MAIN := gui.MainApp

.PHONY: all clean run test

build: $(MARKER)

$(MARKER): $(SOURCES)
	mkdir -p $(BUILD_DIR)
	javac --module-path $$PATH_TO_FX --add-modules $(MODULES) -d $(BUILD_DIR) -cp "$(CLASSPATH)" $(SOURCES)
	touch $(MARKER)

test: build
	AUTORENT_DB_PATH=$(DB_PATH) java -ea\
					 --module-path $$PATH_TO_FX --add-modules $(MODULES)\
					 -cp "$(BUILD_DIR):$(CLASSPATH)" $(TEST) $(MODELS_LIST)

run: build
	AUTORENT_DB_PATH=$(DB_PATH) java -ea --module-path $$PATH_TO_FX --add-modules $(MODULES) -cp "$(BUILD_DIR):$(CLASSPATH)" $(MAIN) $(MODELS_LIST)

clean:
	rm -rf bin
	rm $(DB_PATH)

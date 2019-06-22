/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.streamthoughts.kafka.connect.filepulse.config;

import io.streamthoughts.kafka.connect.filepulse.clean.FileCleanupPolicy;
import io.streamthoughts.kafka.connect.filepulse.scanner.local.FSDirectoryWalker;
import io.streamthoughts.kafka.connect.filepulse.scanner.local.FileListFilter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.streamthoughts.kafka.connect.filepulse.scanner.local.LocalFSDirectoryWalker;
import org.apache.kafka.common.config.ConfigDef;

public class ConnectorConfig extends CommonConfig {

    public static final String FS_SCAN_CLASS_CONFIG           = "fs.scanner.class";
    private static final String FS_SCAN_CLASS_DOC             = "Class which is used to list eligible files from input directory.";

    public static final String FILE_CLEANER_CLASS_CONFIG      = "fs.cleanup.policy.class";
    public static final String FILE_CLEANER_CLASS_DOC         = "The class used to cleanup files that have been processed by tasks.";

    public static final String FS_SCAN_DIRECTORY_PATH_CONFIG  = "fs.scan.directory.path";
    private static final String FS_SCAN_DIRECTORY_PATH_DOC    = "The input directory to scan";

    public static final String FS_SCAN_INTERVAL_MS_CONFIG     = "fs.scan.interval.ms";
    private static final String FS_SCAN_INTERVAL_MS_DOC       = "Time interval in milliseconds at wish the input directory is scanned.";
    private static final long FS_SCAN_INTERVAL_MS_DEFAULT     = 10000L;

    public static final String FS_SCAN_FILTERS_CONFIG         = "fs.scan.filters";
    private static final String FS_SCAN_FILTERS_DOC           = "Filters classes which are used to apply list input files.";

    /**
     * Creates a new {@link ConnectorConfig} instance.
     * @param originals the originals configuration.
     */
    public ConnectorConfig(final Map<?, ?> originals) {
        super(getConf(), originals);
    }

    public static ConfigDef getConf() {
        return CommonConfig.getConf()
                .define(FS_SCAN_CLASS_CONFIG, ConfigDef.Type.CLASS,
                        LocalFSDirectoryWalker.class, ConfigDef.Importance.HIGH, FS_SCAN_CLASS_DOC)

                .define(FS_SCAN_FILTERS_CONFIG, ConfigDef.Type.LIST, Collections.emptyList(),
                        ConfigDef.Importance.MEDIUM, FS_SCAN_FILTERS_DOC)

                .define(FS_SCAN_DIRECTORY_PATH_CONFIG, ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH, FS_SCAN_DIRECTORY_PATH_DOC)

                .define(FS_SCAN_INTERVAL_MS_CONFIG, ConfigDef.Type.LONG, FS_SCAN_INTERVAL_MS_DEFAULT,
                        ConfigDef.Importance.HIGH, FS_SCAN_INTERVAL_MS_DOC)

                .define(FILE_CLEANER_CLASS_CONFIG,
                        ConfigDef.Type.CLASS, ConfigDef.Importance.HIGH, FILE_CLEANER_CLASS_DOC);
    }

    public FileCleanupPolicy cleanupPolicy() {
        return getConfiguredInstance(FILE_CLEANER_CLASS_CONFIG, FileCleanupPolicy.class);
    }

    public FSDirectoryWalker directoryScanner() {
        return getConfiguredInstance(FS_SCAN_CLASS_CONFIG, FSDirectoryWalker.class);
    }

    public long scanInternalMs() {
        return this.getLong(FS_SCAN_INTERVAL_MS_CONFIG);
    }

    public String scanDirectoryPath() {
        return this.getString(FS_SCAN_DIRECTORY_PATH_CONFIG);
    }

    public List<FileListFilter> filters() {
        return getConfiguredInstances(FS_SCAN_FILTERS_CONFIG, FileListFilter.class);
    }
}
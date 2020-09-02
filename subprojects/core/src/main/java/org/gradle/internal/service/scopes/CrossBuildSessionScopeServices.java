/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.internal.service.scopes;

import org.gradle.StartParameter;
import org.gradle.api.internal.CollectionCallbackActionDecorator;
import org.gradle.api.internal.DefaultCollectionCallbackActionDecorator;
import org.gradle.concurrent.ParallelismConfiguration;
import org.gradle.configuration.internal.DefaultListenerBuildOperationDecorator;
import org.gradle.configuration.internal.DefaultUserCodeApplicationContext;
import org.gradle.configuration.internal.ListenerBuildOperationDecorator;
import org.gradle.configuration.internal.UserCodeApplicationContext;
import org.gradle.initialization.DefaultGradleLauncherFactory;
import org.gradle.initialization.GradleLauncherFactory;
import org.gradle.internal.concurrent.DefaultParallelismConfiguration;
import org.gradle.internal.concurrent.ExecutorFactory;
import org.gradle.internal.event.ListenerManager;
import org.gradle.internal.logging.progress.ProgressLoggerFactory;
import org.gradle.internal.logging.sink.OutputEventListenerManager;
import org.gradle.internal.operations.BuildOperationExecutor;
import org.gradle.internal.operations.BuildOperationIdFactory;
import org.gradle.internal.operations.BuildOperationListenerManager;
import org.gradle.internal.operations.BuildOperationProgressEventEmitter;
import org.gradle.internal.operations.DefaultBuildOperationExecutor;
import org.gradle.internal.operations.DefaultBuildOperationQueueFactory;
import org.gradle.internal.operations.logging.LoggingBuildOperationProgressBroadcaster;
import org.gradle.internal.operations.notify.BuildOperationNotificationBridge;
import org.gradle.internal.operations.notify.BuildOperationNotificationValve;
import org.gradle.internal.operations.trace.BuildOperationTrace;
import org.gradle.internal.resources.ResourceLockCoordinationService;
import org.gradle.internal.service.DefaultServiceRegistry;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.internal.time.Clock;
import org.gradle.internal.work.DefaultWorkerLeaseService;
import org.gradle.internal.work.WorkerLeaseService;

import java.io.Closeable;

/**
 * Services to be shared across build sessions.
 *
 * Generally, one regular Gradle invocation is conceptually a session.
 * However, the GradleBuild task is currently implemented in such a way that it uses a discrete session.
 * Having the GradleBuild task reuse the outer session is complicated because it may use a different Gradle user home.
 * See https://github.com/gradle/gradle/issues/4559.
 *
 * This set of services is effectively a mixin, that gets applied to each build session scope.
 */
public class CrossBuildSessionScopeServices implements Closeable {
    private final Services services;

    public CrossBuildSessionScopeServices(ServiceRegistry parent, StartParameter startParameter) {
        this.services = new Services(parent, startParameter);
    }

    public ServiceRegistry getServices() {
        return services;
    }

    @Override
    public void close() {
        services.close();
    }

    private class Services extends DefaultServiceRegistry {

        private final StartParameter startParameter;

        public Services(ServiceRegistry parent, StartParameter startParameter) {
            super(parent);
            this.startParameter = startParameter;
        }

        ParallelismConfiguration createParallelismConfiguration() {
            return new DefaultParallelismConfiguration(startParameter.isParallelProjectExecutionEnabled(), startParameter.getMaxWorkerCount());
        }

        GradleLauncherFactory createGradleLauncherFactory(GradleUserHomeScopeServiceRegistry userHomeDirServiceRegistry) {
            return new DefaultGradleLauncherFactory(
                userHomeDirServiceRegistry,
                CrossBuildSessionScopeServices.this
            );
        }

        WorkerLeaseService createWorkerLeaseService(ResourceLockCoordinationService resourceLockCoordinationService, ParallelismConfiguration parallelismConfiguration) {
            return new DefaultWorkerLeaseService(resourceLockCoordinationService, parallelismConfiguration);
        }

        BuildOperationExecutor createBuildOperationExecutor(
            Clock clock,
            ProgressLoggerFactory progressLoggerFactory,
            WorkerLeaseService workerLeaseService,
            ExecutorFactory executorFactory,
            ParallelismConfiguration parallelismConfiguration,
            BuildOperationIdFactory buildOperationIdFactory,
            BuildOperationListenerManager buildOperationListenerManager
        ) {
            return new DefaultBuildOperationExecutor(
                buildOperationListenerManager.getBroadcaster(),
                clock,
                progressLoggerFactory,
                new DefaultBuildOperationQueueFactory(workerLeaseService),
                executorFactory,
                parallelismConfiguration,
                buildOperationIdFactory
            );
        }

        UserCodeApplicationContext createUserCodeApplicationContext() {
            return new DefaultUserCodeApplicationContext();
        }

        ListenerBuildOperationDecorator createListenerBuildOperationDecorator(BuildOperationExecutor buildOperationExecutor, UserCodeApplicationContext userCodeApplicationContext) {
            return new DefaultListenerBuildOperationDecorator(buildOperationExecutor, userCodeApplicationContext);
        }

        CollectionCallbackActionDecorator createDomainObjectCollectioncallbackActionDecorator(BuildOperationExecutor buildOperationExecutor, UserCodeApplicationContext userCodeApplicationContext) {
            return new DefaultCollectionCallbackActionDecorator(buildOperationExecutor, userCodeApplicationContext);
        }

        LoggingBuildOperationProgressBroadcaster createLoggingBuildOperationProgressBroadcaster(OutputEventListenerManager outputEventListenerManager, BuildOperationProgressEventEmitter buildOperationProgressEventEmitter) {
            return new LoggingBuildOperationProgressBroadcaster(outputEventListenerManager, buildOperationProgressEventEmitter);
        }

        BuildOperationTrace createBuildOperationTrace(StartParameter startParameter, BuildOperationListenerManager buildOperationListenerManager) {
            return new BuildOperationTrace(startParameter, buildOperationListenerManager);
        }

        BuildOperationNotificationBridge createBuildOperationNotificationBridge(BuildOperationListenerManager buildOperationListenerManager, ListenerManager generalListenerManager) {
            return new BuildOperationNotificationBridge(buildOperationListenerManager, generalListenerManager);
        }

        BuildOperationNotificationValve createBuildOperationNotificationValve(BuildOperationNotificationBridge buildOperationNotificationBridge) {
            return buildOperationNotificationBridge.getValve();
        }
    }
}

/*
 * Copyright 2016 the original author or authors.
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

package org.gradle.tooling.internal.provider;

import org.gradle.StartParameter;
import org.gradle.initialization.BuildRequestContext;
import org.gradle.initialization.SessionLifecycleListener;
import org.gradle.internal.event.ListenerManager;
import org.gradle.internal.invocation.BuildAction;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.internal.service.scopes.CrossBuildSessionScopeServices;
import org.gradle.internal.service.scopes.GradleUserHomeScopeServiceRegistry;
import org.gradle.internal.session.BuildSessionState;
import org.gradle.launcher.exec.BuildActionExecuter;
import org.gradle.launcher.exec.BuildActionParameters;
import org.gradle.launcher.exec.BuildActionResult;
import org.gradle.launcher.exec.BuildExecuter;

/**
 * A {@link BuildExecuter} responsible for establishing the session to execute a {@link BuildAction} within.
 */
public class SessionScopeBuildActionExecuter implements BuildActionExecuter<BuildActionParameters, BuildRequestContext> {
    private final ServiceRegistry globalServices;
    private final BuildActionExecuter<BuildActionParameters, BuildSessionContext> delegate;
    private final GradleUserHomeScopeServiceRegistry userHomeServiceRegistry;

    public SessionScopeBuildActionExecuter(GradleUserHomeScopeServiceRegistry userHomeServiceRegistry, ServiceRegistry globalServices, BuildActionExecuter<BuildActionParameters, BuildSessionContext> delegate) {
        this.userHomeServiceRegistry = userHomeServiceRegistry;
        this.globalServices = globalServices;
        this.delegate = delegate;
    }

    @Override
    public BuildActionResult execute(BuildAction action, BuildActionParameters actionParameters, BuildRequestContext requestContext) {
        StartParameter startParameter = action.getStartParameter();
        try (CrossBuildSessionScopeServices crossBuildSessionScopeServices = new CrossBuildSessionScopeServices(globalServices, startParameter)) {
            try (BuildSessionState buildSessionState = new BuildSessionState(userHomeServiceRegistry, crossBuildSessionScopeServices, startParameter, requestContext, actionParameters.getInjectedPluginClasspath(), requestContext.getCancellationToken(), requestContext.getClient(), requestContext.getEventConsumer())) {
                SessionLifecycleListener sessionLifecycleListener = buildSessionState.getServices().get(ListenerManager.class).getBroadcaster(SessionLifecycleListener.class);
                try {
                    sessionLifecycleListener.afterStart();
                    return delegate.execute(action, actionParameters, new BuildSessionContext(requestContext, buildSessionState));
                } finally {
                    sessionLifecycleListener.beforeComplete();
                }
            }
        }
    }
}

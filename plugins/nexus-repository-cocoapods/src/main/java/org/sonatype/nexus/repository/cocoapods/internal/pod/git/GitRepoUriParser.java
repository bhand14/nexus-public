/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.repository.cocoapods.internal.pod.git;

import java.net.URI;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @since 3.19
 */
public class GitRepoUriParser
{
  private static final String GIT_REPO_EXTENSION = ".git";

  private static final Integer VENDOR_POSITION = 1;

  private static final Integer REPO_POSITION = 2;

  private static final int GIT_PATH_SEGMENTS_COUNT = 3;

  private GitRepoUriParser(){}

  public static GitArtifactInfo parseGitRepoUri(final URI gitRepoUri, @Nullable final String ref) {
    checkArgument(isRepoSupported(gitRepoUri), "repository not supported: " + gitRepoUri);

    checkArgument(isGitUriFormatSupported(gitRepoUri), "invalid git path: " + gitRepoUri);

    String gitPath = gitRepoUri.getPath();
    if (gitPath.endsWith(GIT_REPO_EXTENSION)) {
      gitPath = gitPath.substring(0, gitPath.length() - GIT_REPO_EXTENSION.length());
    }

    String[] segments = gitPath.split("/");
    return new GitArtifactInfo(gitRepoUri.getHost(), segments[VENDOR_POSITION], segments[REPO_POSITION], ref);
  }

  public static boolean isGitUriFormatSupported(final URI repoUri) {
    return repoUri.getPath().split("/").length == GIT_PATH_SEGMENTS_COUNT;
  }

  public static boolean isRepoSupported(final URI repoUri) {
    return repoUri.getHost().equals(GitConstants.GITHUB_HOST) || repoUri.getHost().equals(GitConstants.BITBUCKET_HOST);
  }
}

// Copyright 2016-present 650 Industries. All rights reserved.

#import <UMCore/UMExportedModule.h>
#import <UMCore/UMModuleRegistryConsumer.h>

#import <UMCore/UMUIManager.h>
#import <ExpoModulesCore/EXFileSystemInterface.h>

@interface EXGLObjectManager : UMExportedModule <UMModuleRegistryConsumer>

@property (nonatomic, weak, nullable) id<UMUIManager> uiManager;
@property (nonatomic, weak, nullable) id<EXFileSystemInterface> fileSystem;

- (void)saveContext:(nonnull id)glContext;
- (void)deleteContextWithId:(nonnull NSNumber *)contextId;

@end

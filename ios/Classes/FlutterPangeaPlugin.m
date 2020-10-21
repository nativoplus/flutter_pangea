#import "FlutterPangeaPlugin.h"
#if __has_include(<flutter_pangea/flutter_pangea-Swift.h>)
#import <flutter_pangea/flutter_pangea-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_pangea-Swift.h"
#endif

@implementation FlutterPangeaPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterPangeaPlugin registerWithRegistrar:registrar];
}
@end

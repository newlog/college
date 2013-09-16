#import "EstelAppDelegate.h"
#import "EstelViewController.h"
#import "Music.h"
#import "Main.h"
#import "Configuration.h"

@implementation EstelAppDelegate

@synthesize window;
@synthesize viewController;

@synthesize controlTab;
@synthesize vista1;
@synthesize vista2;
//@synthesize con;

#pragma mark -
#pragma mark Application lifecycle

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {    

    // Override point for customization after application launch.
	
	Main * tab1Vista1 = [[[Main alloc] init] autorelease];
	tab1Vista1.title = @MUSICTABTITLE;
	vista1 = [[UINavigationController alloc] initWithRootViewController:tab1Vista1];
	vista1.tabBarItem.title = @MUSICTAB;
	//vista1.tabBarItem.image = [UIImage imageNamed:@"112-group.png"];
	
	//Here we instantiate the Connection class to pass its reference to the other classes
	//con = [[Connection alloc] init];
	
	Music * tab2Vista1 = [[[Music alloc] init] autorelease];
	tab2Vista1.title = @CONFTABTITLE;
	vista2 = [[UINavigationController alloc] initWithRootViewController:tab2Vista1];
	vista2.tabBarItem.title = @CONFTAB;
	//vista2.tabBarItem.image = [UIImage imageNamed:@"13-target.png"];
	
	// We set the Music class as an observer. This is done to reload the table contents with the song names
	[[NSNotificationCenter defaultCenter] addObserver:tab2Vista1 selector:@selector(songNamesReady:) name:@"SongNamesReady" object:nil];
	
	controlTab = [[UITabBarController alloc] init];
	controlTab.viewControllers = [[NSArray alloc] initWithObjects: vista1, vista2, nil];	
	controlTab.delegate = self;
	[controlTab.view setBackgroundColor: [UIColor whiteColor]];
	
	[window addSubview:controlTab.view];
	[self.window makeKeyAndVisible];
	
	
    return YES;
}


- (void)applicationWillResignActive:(UIApplication *)application {
    /*
     Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
     Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
     */
}


- (void)applicationDidEnterBackground:(UIApplication *)application {
    /*
     Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
     If your application supports background execution, called instead of applicationWillTerminate: when the user quits.
     */
}


- (void)applicationWillEnterForeground:(UIApplication *)application {
    /*
     Called as part of  transition from the background to the inactive state: here you can undo many of the changes made on entering the background.
     */
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    /*
     Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
     */
}


- (void)applicationWillTerminate:(UIApplication *)application {
    /*
     Called when the application is about to terminate.
     See also applicationDidEnterBackground:.
     */
}


#pragma mark -
#pragma mark Memory management

- (void)applicationDidReceiveMemoryWarning:(UIApplication *)application {
    /*
     Free up as much memory as possible by purging cached data objects that can be recreated (or reloaded from disk) later.
     */
}


- (void)dealloc {
	
	[controlTab.viewControllers release];
	[vista1 release];
	[vista2 release];
	[controlTab release];
//	[con release];
	
    [viewController release];
    [window release];
    [super dealloc];
}


@end

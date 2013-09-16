//
//  Main.m
//  Estel
//
//  Created by Beleriand on 15/03/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "Main.h"


@implementation Main


@synthesize menuView;


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
	
	//The menu UIView is invoked
	menuView = [[Menu alloc] initWithFrame: CGRectMake(0.0, 0.0, SCREEN_WIDTH, SCREEN_HEIGTH)];
	//The Main view is setted to be the menu view (without this, the UI is not shown).
	self.view = menuView;
	//The menu view is released. It can be done because the Main view is already setted to the menu view.
	[menuView release];

}


/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations.
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc. that aren't in use.
}

- (void)viewDidUnload {
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}


- (void)dealloc {
    [super dealloc];
}


@end

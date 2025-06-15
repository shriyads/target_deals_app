## Android Case Study

You have been given control over an Android project that was originally a proof-of-concept project. The original 
developer of the project has since moved on to a new team, and Target has asked you to turn the project into an 
application that the company could release to the public.

The goal of the app is to display a list of deals currently offered by Target, and to provide information on those
 deals. As a POC, the app has a few deals hardcoded in and the code is pretty rough. It is your job to turn this app into something useful!

#### Requirements
1. Fix up the deals list to match the mockups shown in [mockups](https://www.figma.com/file/hIHMSLgHFhWMyQfVp8fZHc/Android-Technical-Screener). 
   Do your best to match the text sizes, colors, and margins from the mockups.  We don't expect your implementation to be pixel-perfect.

2. Present a new view that displays deal details when a deal is clicked on the list screen. Use the [mockups](https://www.figma.com/file/hIHMSLgHFhWMyQfVp8fZHc/Android-Technical-Screener). 
   Again, match text sizes, colors, and margins as best you can.

3. The deals are currently hardcoded. Use the API at [https://api.target.com/mobile_case_study_deals/v1](https://api.target.com/mobile_case_study_deals/v1) 
to grab the real deals to display in the app.  Your solution should make use of both of the API endpoints.

#### Guidelines
- For convenience, the project uses libraries like [Retrofit](https://square.github.io/retrofit/), [Dagger](https://dagger.dev/) 
  and [Glide](https://bumptech.github.io/glide/).  Feel free to add other open source libraries or replace the existing libraries.  
  Be prepared to answer questions about _why_ you chose any libraries that you add to the project.
- This project was only tested minimally during development.  There may be bugs in the current implementation.
- Imagine that this app will continue to grow after you are done.  Consider and be ready to discuss how the following engineering values fit into your chosen solution:
	- Testability (Make sure the run the existing test suite!)
	- Reusability
	- Clean, well organized code
	- Error handling
	- Design accuracy on multiple screen sizes
- Do your best to follow modern Android conventions.
- You have the freedom to take this project in whatever direction you'd like; you can keep as much 
or as little of the existing code as you'd like.

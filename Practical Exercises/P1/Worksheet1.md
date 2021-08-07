# Worksheet 1

## Question 1

### (a)

Could compromise the system and cause catastrophic problems on the plane.

No possibility for technical support on a plane.

Plugins could slow down the software.

### (b)

That's terribly time consuming to work off a remote server for large files like
video files. SQL is also not suited to these operations so is going to be
tedious to code, and slow to run.

### (c)

C is missing many of the features that other frameworks/languages include that
make them better suited to a social-media app.

Need to deal with memory management.

### (d)

Runs on Windows only (mostly).

Maybe run it on a web app instead of this framework.

## Question 2

### Stovepipe system

Maintainability is worse. Less friendly when working across services.

### Vendor lock-in

Tied-in to use a vendor's service/platform/system. If they go under or kill the
platform you have to essentially start again.

## Question 4

### Diagram

![Diagram](Practical Exercises\P1\Architecture Diagram.png)

### Frontend

1. Scriptable web app with a default look that walks users through a survey.
Web app to be built with whatever framework the government agency developers
are familiar with, but React.js is a good modern option, or Vue.js if there
are concerns about vendor lock-in from using a Facebook product.
(Web app is platform-independant to accomodate for anyone with a computer and
internet access, scriptability allows agencies to extend the functionality of
the site to accommodate a survey's specific needs).

2. Direct databse entry for agency employees entering paper-based survey
results via Microsoft Access (Government agencies use a standardised Windows
environment, employees can *probably* be trusted with this level of access).

3. Paper form given to users with no computer access.

4. Survey data access via Microsoft Access (Government agencies use a
standardised Windows environment, Access can apparently do charts).

### Authentication

1. Existing government username/password authentication system for users on the
web app form.

2. Internal agency employee authentication for agency employees on the
spreadsheet and data access web app.

### Backend

Network separated database that provides database access to privileged users
all tied into a relational Microsoft Access (Because they use Windows and 
relational is flexible).

### Database

Separate database table for each kind of survey, to accommodate for different
structures of data for different surveys. Paper based surveys and normally
filled out surveys are indistinguishable on the database.

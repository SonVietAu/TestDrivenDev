*** Test Driven Development

This project demonstrates test driven development (TDD) as Google recommended using Stock Market Data API as a data source. Basic company information and real-time price information are two sets of data that will be displayed. 

The app at the highest level will work as follow:
- Starting from a CompaniesFragment, users will add companies that they like to keep track of one by one via a SearchCompanyDialogFragment. 
- Once found, users can either add the company or view the company in a CompanyInformationFragment. Users can then add the company or conduct another search.
- The CompaniesFragment will display the real-time price but also allow users to open to the CompanyInformationFragment and maybe to a KeyStatsFragment

The app will use MVVM with a Room database that is accessible via a repository class. The app will be structured to allow for unit, integration and end-to-end tests. TDD focus on unit tests (80% of tests as recommended by Google) and hence will require a bottom-up build. This project will start with the CompanyRepository. 


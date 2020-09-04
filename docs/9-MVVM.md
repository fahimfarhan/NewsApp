# MVVM
Finally we are gonna use view model in our project. All the data so far are in the main activity class.
But under certain situations, say the user is frequently rotating the device, the activity recreates itself
and so it the same data needs to be loaded again and again. This is bad. So we simply move all our
working data inside `ViewModel` / `AndroidViewModel` as data inside the viewModel can survive beyond
activity lifecycle. For large projects, it is a good idea to keep the data in a `Repository` class and
create an object of that `Repositorty` inside the `VideModel` class.

So I simply created NewsRepository, moved all the data inside there. Then create a ViewModel, and create
a NewsRepository object. Finally, in the main activity, I am accessing the data through the viewModel.
Check out the codes.
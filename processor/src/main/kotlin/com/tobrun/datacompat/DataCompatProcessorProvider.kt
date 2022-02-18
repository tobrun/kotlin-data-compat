import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.tobrun.datacompat.DataCompatProcessor

@AutoService(SymbolProcessorProvider::class)
class DataCompatProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return DataCompatProcessor(
            environment.codeGenerator,
            environment.logger,
            environment.options
        )
    }
}